package frc.robot.subsystems.swerve.constants;

import static edu.wpi.first.units.Units.FeetPerSecond;
import static edu.wpi.first.units.Units.MetersPerSecondPerSecond;
import static edu.wpi.first.units.Units.Pounds;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.sim.TalonFXSimState.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Mass;
import frc.robot.subsystems.swerve.module.Module.ModuleConstants;

public class KitbotSwerveConstants extends SwerveConstants {

    @Override
    public String getName() {
       return "Kitbot";
    }

    @Override
    public double getTrackWidthX() {
        return Units.inchesToMeters(21.75);
    }

    @Override
    public double getTrackWidthY() {
        return Units.inchesToMeters(21.75);

    }

    @Override
    public double getBumperWidth() {
        return Units.inchesToMeters(27.00);
    }

    @Override
    public double getBumperLength() {
         return Units.inchesToMeters(27.00);
    }

    @Override
    public double getMaxLinearSpeed() {
     return Units.feetToMeters(16.8); 
    }

    @Override
    public double getMaxLinearAcceleration() {
        return 14.6;
    // This is not an exact value
    }

    @Override
    public double getDriveGearRatio() {
        return 7.03;
    }

    @Override
    public double getTurnGearRatio() {
        return 5.27;
        // got this from https://www.swervedrivespecialties.com/products/mk5n-swerve-module
    }

    @Override
    public Mass getMass() {
        // 16 lb battery, 15 lb bumpers (might change)
        return Pounds.of(77.186 + 16.0 + 15.0);
    }

    // TODO: CANCODER OFFSETS

 @Override
  public ModuleConstants getFrontLeftModuleConstants() {
    return new ModuleConstants(
        0, "Front Left", 0, 1, 0, Rotation2d.fromRotations(-0.29).plus(Rotation2d.k180deg));
  }

  @Override
  public ModuleConstants getFrontRightModuleConstants() {
    return new ModuleConstants(1, "Front Right", 2, 3, 1, Rotation2d.fromRotations(0.012));
  }

  @Override
  public ModuleConstants getBackLeftModuleConstants() {
    return new ModuleConstants(
        2, "Back Left", 4, 5, 2, Rotation2d.fromRotations(0.229).plus(Rotation2d.k180deg));
  }

  @Override
  public ModuleConstants getBackRightModuleConstants() {
    return new ModuleConstants(3, "Back Right", 6, 7, 3, Rotation2d.fromRotations(-0.205));
  }



    @Override
    public int getGyroID() {
return 0;
    }

    @Override
  public Pigeon2Configuration getGyroConfig() {
    Pigeon2Configuration config = new Pigeon2Configuration();
    // TODO: CALIB GYRO
    config.MountPose.MountPosePitch = 1.1281073093414307;
    config.MountPose.MountPoseRoll = -0.039543408900499344;
    config.MountPose.MountPoseYaw = 89.08586883544922;
    return config;
  }

  @Override
  public TalonFXConfiguration getDriveConfig() {
    var driveConfig = new TalonFXConfiguration();
    // Current limits
    driveConfig.CurrentLimits.SupplyCurrentLimit = 40.0;
    driveConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    driveConfig.CurrentLimits.StatorCurrentLimit = 120.0;
    driveConfig.CurrentLimits.StatorCurrentLimitEnable = true;
    // Inverts
    driveConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    driveConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    // Sensor
    // Meters per second
    driveConfig.Feedback.SensorToMechanismRatio = getDriveRotorToMeters();
    // Current control gains
    // Gains copied from Kelpie Swerve Constants
    // May need tuning
    driveConfig.Slot0.kV = 5.0;
    // kT (stall torque / stall current) converted to linear wheel frame
    driveConfig.Slot0.kA = 0.0; // (9.37 / 483.0) / getDriveRotorToMeters(); // 3.07135116146;
    driveConfig.Slot0.kS = 10.0;
    driveConfig.Slot0.kP = 300.0;
    driveConfig.Slot0.kD = 0.0; // 1.0;

    driveConfig.TorqueCurrent.TorqueNeutralDeadband = 10.0;

    driveConfig.MotionMagic.MotionMagicCruiseVelocity = getMaxLinearSpeed();
    driveConfig.MotionMagic.MotionMagicAcceleration = getMaxLinearAcceleration();

    return driveConfig;
  }

  @Override
  public TalonFXConfiguration getTurnConfig(int cancoderID) {
    var turnConfig = new TalonFXConfiguration();
    // Current limits
    turnConfig.CurrentLimits.SupplyCurrentLimit = 20.0;
    turnConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    // Inverts
    turnConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    turnConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    // Fused Cancoder
    turnConfig.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder;
    turnConfig.Feedback.FeedbackRemoteSensorID = cancoderID;
    turnConfig.Feedback.RotorToSensorRatio = getTurnGearRatio();
    turnConfig.Feedback.SensorToMechanismRatio = 1.0;
    turnConfig.Feedback.FeedbackRotorOffset = 0.0;

    // turnConfig.Feedback.SensorToMechanismRatio = getTurnGearRatio();

    // turnConfig.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;
    // turnConfig.Feedback.FeedbackRemoteSensorID = cancoderID;
    // turnConfig.Feedback.RotorToSensorRatio = getTurnGearRatio();
    // turnConfig.Feedback.SensorToMechanismRatio = 1.0;
    // turnConfig.Feedback.FeedbackRotorOffset = 0.0;

    // Controls Gains
    turnConfig.Slot0.kV = 2.557;
    turnConfig.Slot0.kA = 0.07029;
    turnConfig.Slot0.kS = 0.073114;
    turnConfig.Slot0.kP = 100.0; // 300.0; // 250.0;
    turnConfig.Slot0.kD = 1.0; // 0.5;
    turnConfig.MotionMagic.MotionMagicCruiseVelocity =
        (7368.0 / 60.0) / getTurnGearRatio(); // TODO: VERIFY TUNING AGAIN!
    turnConfig.MotionMagic.MotionMagicAcceleration = (7368.0 / 60.0) / (getTurnGearRatio() * 0.005);
    turnConfig.ClosedLoopGeneral.ContinuousWrap = true;

    return turnConfig;
  }

  @Override
  public CANcoderConfiguration getCancoderConfig(Rotation2d cancoderOffset) {
    final var cancoderConfig = new CANcoderConfiguration();
    cancoderConfig.MagnetSensor.MagnetOffset = cancoderOffset.getRotations();
    cancoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
    return cancoderConfig;
  }

    @Override
    public MotorType getTurnMotorType() {
        return MotorType.KrakenX44;
    }

    @Override
    public MotorType getDriveMotorType() {
        return MotorType.KrakenX60;
    }

    @Override
    public double getHeadingVelocityKP() {
        return 6.0;
    }

}
