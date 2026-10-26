// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.indexer;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.sim.TalonFXSimState;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerIOSim extends IndexerIO {
  /** Creates a new IndexerIOSim. */
  protected TalonFX leftIndexerMotor;
  protected TalonFX rightIndexerMotor;
  // TODO: add a config variable for the motor ids in IndexerSubsystem instead of hardcoding it

  TalonFXSimState leftFxSimState;
  TalonFXSimState rightFxSimState;
  private double lastSimTime = 0.0;
  private Notifier notifier;
  
  public IndexerIOSim(CANBus canbus) {
    super(canbus);
    leftFxSimState = leftIndexerMotor.getSimState();
    rightFxSimState = rightIndexerMotor.getSimState();
    // TODO: finish this whole thing idk if TalonFX is the right class for the motors
  }

}
