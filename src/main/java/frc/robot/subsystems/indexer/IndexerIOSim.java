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
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotGearing;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotMotor;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotWheelSize;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerIOSim extends IndexerIO {
  /** Creates a new IndexerIOSim. */
  protected TalonFX indexerMotor;
  // TODO: add a config variable for the motor ids in IndexerSubsystem instead of hardcoding it

  TalonFXSimState indexerFxSimState;
  DCMotorSim physicsSimIndexer;
  private double simLoopPeriod = 0.002;
  private Notifier notifier;
  private double lastSimTime = 0.0;
  public IndexerIOSim(CANBus canbus) {
    super(canbus);
    
    physicsSimIndexer =
      new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getKrakenX60Foc(1), 0, 0),
            // Fix the gear ratio and meter thing
            DCMotor.getKrakenX60Foc(1));
    indexerFxSimState = indexerMotor.getSimState();
    indexerFxSimState.setMotorType(MotorType.KrakenX60);
    indexerFxSimState.Orientation = ChassisReference.CounterClockwise_Positive; // check if this is right

    notifier = 
        new Notifier(
          () -> {
            final double currentTime = Timer.getTimestamp();
            final double deltaTime = currentTime - lastSimTime;
            lastSimTime = currentTime;

            indexerFxSimState.setSupplyVoltage(RobotController.getBatteryVoltage());
            physicsSimIndexer.setInputVoltage(indexerFxSimState.getMotorVoltage());
            indexerFxSimState.setRawRotorPosition(
                  physicsSimIndexer.getAngularPosition().in(Rotations) * 0); // add gear ratio
              indexerFxSimState.setRotorVelocity(
                  physicsSimIndexer.getAngularVelocity().in(RotationsPerSecond) * 0); // add gear ratio
          });


    // TODO: finish this whole thing idk if TalonFX is the right class for the motors
    notifier.setName("Indexer");
    notifier.startPeriodic(simLoopPeriod);
  }

}
