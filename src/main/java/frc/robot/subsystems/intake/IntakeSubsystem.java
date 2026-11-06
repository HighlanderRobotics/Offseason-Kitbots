// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.CANBus;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem(CANBus canBus) {} // satisfy requirement for robot.java
  public Command setVoltagesCommand(DoubleSupplier value) {
    return new Command() {};
  }
  public Command stopIntake() {
    return new Command() {};
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
