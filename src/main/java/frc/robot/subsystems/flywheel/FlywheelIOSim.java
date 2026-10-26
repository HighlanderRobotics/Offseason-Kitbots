// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.flywheel;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.sim.TalonFXSimState;

import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.LinearSystem;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;


public class FlywheelIOSim extends FlywheelIO {

  TalonFXSimState leaderTalonSimState;
  private DCMotor flywheelMechanismMotor;

  // sim timing instance variables
  private double lastSimTime = 0.0;
  private Notifier notifier;
  
  // need to create some kind of flywheelSim, but what would a linear system have to do with anything?
  private LinearSystem<N1,N1,N1> flywheelMotorLinearSystem = LinearSystemId.createFlywheelSystem(flywheelMechanismMotor, 1, 10);
  private FlywheelSim wheelSim = new FlywheelSim(flywheelMotorLinearSystem, flywheelMechanismMotor, null); 

  public FlywheelIOSim(CANBus canBus) {
    super(canBus);
    
    leaderTalonSimState = leader.getSimState();

    notifier = new Notifier(() -> {
      
      //calculate delta
      double currentTime = Timer.getTimestamp();
      double deltaTime = currentTime - lastSimTime;
      lastSimTime = currentTime;

      leaderTalonSimState.setSupplyVoltage(RobotController.getBatteryVoltage());
      wheelSim.setInputVoltage(leaderTalonSimState.getMotorVoltage());
      wheelSim.update(deltaTime);

      // "RadPerSecSq", why not just call it rps2 or something shorder
      wheelSim.setAngularVelocity(wheelSim.getAngularAccelerationRadPerSecSq());
    });
    notifier.startPeriodic(0.002);
  } 
}
