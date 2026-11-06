// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.indexer;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.CANBus;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerSubsystem extends SubsystemBase {
  /** Creates a new IndexerSubsystem. */
  public IndexerSubsystem(CANBus canBus) {}

  public Command setVoltageCommand(DoubleSupplier voltage) {
    return this.idle();
  }
  public Command stopIndexer() {
    return this.idle();
  };
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
