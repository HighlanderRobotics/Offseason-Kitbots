package frc.robot.subsystems.swerve;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static edu.wpi.first.units.Units.Inch;
import static edu.wpi.first.units.Units.KilogramSquareMeters;
import static edu.wpi.first.units.Units.Meter;
import static edu.wpi.first.units.Units.Volts;
import choreo.trajectory.SwerveSample;
import com.ctre.phoenix6.CANBus;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Config;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Mechanism;
import frc.robot.Robot;
import frc.robot.subsystems.swerve.gyro.GyroIO;
import frc.robot.subsystems.swerve.gyro.GyroIOReal;
import frc.robot.subsystems.swerve.gyro.GyroIOSim;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import org.ironmaple.simulation.SimulatedArena;
import org.ironmaple.simulation.drivesims.COTS;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;
import org.ironmaple.simulation.drivesims.SwerveModuleSimulation;
import org.ironmaple.simulation.drivesims.configs.DriveTrainSimulationConfig;
import org.ironmaple.simulation.drivesims.configs.SwerveModuleSimulationConfig;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;
public class SwerveSubsystems extends SubsystemBase{
    
//   private final Module[] modules; // Front Left, Front Right, Back Left, Back Right
//   private final GyroIO gyroIO;                                                           DO THIS!!
//   private final GyroIOInputsAutoLogged gyroInputs = new GyroIOInputsAutoLogged();
//    private final OdometryThreadIO odometryThread;
//   private final OdometryThreadIOInputs odometryThreadInputs = new OdometryThreadIOInputs();
  private double lastOdometryUpdateTimestamp = 0.0;
    private SwerveDriveKinematics kinematics;

  private SwerveDrivePoseEstimator estimator;

  private SwerveModulePosition[] lastModulePositions =
      new SwerveModulePosition[] {
        new SwerveModulePosition(),
        new SwerveModulePosition(),
        new SwerveModulePosition(),
        new SwerveModulePosition()
      };
    private Rotation2d rawGyroRotation = new Rotation2d();
  private Alert usingSyncOdoAlert = new Alert("Using Sync Odometry", AlertType.kInfo);
  private Alert missingModuleData = new Alert("Missing Module Data", AlertType.kError);
  private Alert missingGyroData = new Alert("Missing Gyro Data", AlertType.kWarning);
    private final SwerveModuleSimulationConfig mk5nModuleConfig =
      new SwerveModuleSimulationConfig(
          DCMotor.getKrakenX60Foc(1),
          DCMotor.getKrakenX44Foc(1),
          SwerveSubsystem.SWERVE_CONSTANTS.getDriveGearRatio(),
          SwerveSubsystem.SWERVE_CONSTANTS.getTurnGearRatio(),
          Volts.of(0.1),
          Volts.of(0.2),
          Inch.of(2),
          KilogramSquareMeters.of(0.03),
          2.25);
  // ripped the drive friction voltage, steer friction voltage, and steer rotational inertia from
  // the mk4n- no clue if that's right lmao
  // Maple Sim Stuff
  // TODO: UPDATE DRIVETRAIN SIM!!!
  private final DriveTrainSimulationConfig driveTrainSimConfig =
      DriveTrainSimulationConfig.Default()
          .withGyro(COTS.ofPigeon2())
          .withSwerveModule(
              // COTS.ofMark4n(
              //     DCMotor.getKrakenX60Foc(1),
              //     DCMotor.getKrakenX60Foc(1),
              //     // Still not sure where the 1.5 came from
              //     1.5,
              //     // Running l2+ swerve modules
              //     2))
              () -> new SwerveModuleSimulation(mk5nModuleConfig))
          .withTrackLengthTrackWidth(
              Meter.of(SwerveSubsystem.SWERVE_CONSTANTS.getTrackWidthX()),
              Meter.of(SwerveSubsystem.SWERVE_CONSTANTS.getTrackWidthY()))
          .withBumperSize(
              Meter.of(SwerveSubsystem.SWERVE_CONSTANTS.getBumperWidth()),
              Meter.of(SwerveSubsystem.SWERVE_CONSTANTS.getBumperLength()))
          .withRobotMass(SwerveSubsystem.SWERVE_CONSTANTS.getMass());

  private final SwerveDriveSimulation swerveSimulation =
      new SwerveDriveSimulation(driveTrainSimConfig, new Pose2d(3, 3, Rotation2d.kZero));

  public SwerveSubsystem(CANBus canbus) {
    if (Robot.ROBOT_MODE == RobotMode.SIM) {
      // Add simulated modules
      modules =
          new Module[] {
            new Module(
                new ModuleIOSim(
                    SWERVE_CONSTANTS.getFrontLeftModuleConstants(),
                    swerveSimulation.getModules()[0],
                    canbus)),
            new Module(
                new ModuleIOSim(
                    SWERVE_CONSTANTS.getFrontRightModuleConstants(),
                    swerveSimulation.getModules()[1],
                    canbus)),
            new Module(
                new ModuleIOSim(
                    SWERVE_CONSTANTS.getBackLeftModuleConstants(),
                    swerveSimulation.getModules()[2],
                    canbus)),
            new Module(
                new ModuleIOSim(
                    SWERVE_CONSTANTS.getBackRightModuleConstants(),
                    swerveSimulation.getModules()[3],
                    canbus))
          };
   
          };
    } else {
      // Add real modules
      modules =
          new Module[] {
            new Module(new ModuleIOReal(SWERVE_CONSTANTS.getFrontLeftModuleConstants(), canbus)),
            new Module(new ModuleIOReal(SWERVE_CONSTANTS.getFrontRightModuleConstants(), canbus)),
            new Module(new ModuleIOReal(SWERVE_CONSTANTS.getBackLeftModuleConstants(), canbus)),
            new Module(new ModuleIOReal(SWERVE_CONSTANTS.getBackRightModuleConstants(), canbus))
          };
}
