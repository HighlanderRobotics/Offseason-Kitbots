// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.flywheel;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FlywheelSubsystem extends SubsystemBase {
  private FlywheelIO io;
  /** Creates a new FlywheelSubsystem. */
  public FlywheelSubsystem(CANBus canBus) {
    io = new FlywheelIO(canBus);
  }

  public Command runAtVelocity(DoubleSupplier velocity) {
    return this.run(() -> io.setVelocitySetpoint(velocity.getAsDouble()));
  }
  public Command runAtVoltage(DoubleSupplier voltage) {
    return this.run(() -> io.setVoltage(voltage.getAsDouble()));
  }

  public Command stopFlywheel() {
    return this.run(() -> io.setVoltage(0.0));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public static TalonFXConfiguration getFlywheelConfig() {
    TalonFXConfiguration config = new TalonFXConfiguration();

    config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    config.MotorOutput.NeutralMode = NeutralModeValue.Coast;

    config.Feedback.SensorToMechanismRatio = 1.0; // TODO

    config.CurrentLimits.StatorCurrentLimit = 30.0;
    config.CurrentLimits.StatorCurrentLimitEnable = true;

    config.CurrentLimits.SupplyCurrentLimit = 30.0;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;

    config.Slot0.kA = 0.0;
    config.Slot0.kD = 0.0;
    config.Slot0.kI = 0.0;
    config.Slot0.kP = 0.0;
    config.Slot0.kS = 0.0;
    config.Slot0.kV = 0.0;

    return config;
  }
}
