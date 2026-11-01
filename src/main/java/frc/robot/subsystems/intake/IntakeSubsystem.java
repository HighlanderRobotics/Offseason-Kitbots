// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import java.util.function.DoubleSupplier;
import java.util.random.RandomGenerator.LeapableGenerator;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new IntakeSubsystem. */
  RollerIO io;

  public static final int LEFT_TALON_ID = 0;
  public static final int RIGHT_TALON_ID = 1;

  VoltageOut RollVoltage = new VoltageOut(0);

  TalonFX Roller;

  public IntakeSubsystem(CANBus canbus) {
        this.io = Robot.isReal() ? new RollerIO(canbus) : new RollerIOSim(canbus);
    }

  public void setVolts(double value) {
        Roller.setControl(RollVoltage.withOutput(value));
}
  public Command setVoltagesCommand(DoubleSupplier value) {
  return this.run(() -> io.setVolts(value.getAsDouble()));
    }
    public static TalonFXConfiguration getFlywheelConfig() {
    TalonFXConfiguration config = new TalonFXConfiguration();

    config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    config.MotorOutput.NeutralMode = NeutralModeValue.Coast;

    config.Feedback.SensorToMechanismRatio = 1.0;

    config.CurrentLimits.StatorCurrentLimit = 30.0;
    config.CurrentLimits.StatorCurrentLimitEnable = true;

    config.CurrentLimits.SupplyCurrentLimit = 30.0;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;

    // todo: get actual values sometime
    config.Slot0.kA = 0.0;
    config.Slot0.kD = 0.0;
    config.Slot0.kI = 0.0;
    config.Slot0.kP = 0.0;
    config.Slot0.kS = 0.0;
    config.Slot0.kV = 0.0;

    return config;
  }
}
