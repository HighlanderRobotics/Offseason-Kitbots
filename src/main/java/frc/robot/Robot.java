// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import com.ctre.phoenix6.CANBus;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.flywheel.FlywheelSubsystem;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import frc.robot.utils.CommandXboxControllerSubsystem;

public class Robot extends LoggedRobot {
  public enum RobotMode {
    REAL, SIM, REPLAY;
  }

  public static final RobotMode ROBOT_MODE = Robot.isReal() ? RobotMode.REAL : RobotMode.SIM;

  private CANBus canbus = new CANBus("*");

  private SwerveSubsystem swerve = new SwerveSubsystem(canbus);

  private CommandXboxControllerSubsystem driver = new CommandXboxControllerSubsystem(0);

  public Robot() {

    Logger.recordMetadata("ProjectName", "John Kitbot"); // props to simon for the beutiful name

  if (isReal()) {
      Logger.addDataReceiver(new WPILOGWriter()); // Log to a USB stick ("/U/logs")
      Logger.addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
  } else {
      setUseTiming(false); // Run as fast as possible
      String logPath = LogFileUtil.findReplayLog(); // Pull the replay log from AdvantageScope (or prompt the user)
      Logger.setReplaySource(new WPILOGReader(logPath)); // Read replay log
      Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim"))); // Save outputs to a new log
  }

  Logger.start(); // Start logging! No more data receivers, replay sources, or metadata values may be added.

  // controller.a().whileTrue(flywheel.runAtVoltage(() -> 10.0)); // todo: get controller layour from bora

  swerve.setDefaultCommand(
        swerve
            .driveOpenLoopFieldRelative(
                () ->
                    new ChassisSpeeds(
                            modifyJoystick(driver.getLeftY())
                                * SwerveSubsystem.SWERVE_CONSTANTS.getMaxLinearSpeed(),
                            modifyJoystick(driver.getLeftX())
                                * SwerveSubsystem.SWERVE_CONSTANTS.getMaxLinearSpeed(),
                            modifyJoystick(driver.getRightX())
                                * SwerveSubsystem.SWERVE_CONSTANTS.getMaxAngularSpeed())
                        .times(-1))
            .withName("Teleop drive"));

  driver
        .rightStick()
        .and(driver.leftStick())
        .onTrue(Commands.runOnce(() -> swerve.headingReset(Rotation2d.kZero)));
}

/** Scales a joystick value for teleop driving */
  private static double modifyJoystick(double val) {
    return MathUtil.applyDeadband(Math.abs(Math.pow(val, 2)) * Math.signum(val), 0.02);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
