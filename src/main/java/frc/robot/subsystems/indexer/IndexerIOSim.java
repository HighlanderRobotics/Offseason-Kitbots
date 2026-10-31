// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.indexer;

import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.sim.ChassisReference;
import com.ctre.phoenix6.sim.TalonFXSimState;
import com.ctre.phoenix6.sim.TalonFXSimState.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
// import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
// import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotGearing;
// import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotMotor;
// import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotWheelSize;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerIOSim extends SubsystemBase {
  /** Creates a new IndexerIOSim. */
  public IndexerIOSim() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
