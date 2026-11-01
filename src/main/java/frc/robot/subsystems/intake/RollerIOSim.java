// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.sim.TalonFXSimState;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotGearing;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotMotor;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotWheelSize;

public class RollerIOSim extends RollerIO {
  /** Creates a new RollerIOSim. */
    private double lastSimTime = 0.0;
    private Notifier notifier;
    TalonFXSimState SimState;
    public RollerIOSim(CANBus canbus) {
    super(canbus);
    SimState = leftTalon.getSimState();
    DifferentialDrivetrainSim physicsSim = DifferentialDrivetrainSim.createKitbotSim(
    KitbotMotor.kSingleFalcon500PerSide,
    KitbotGearing.k8p45,
    KitbotWheelSize.kSixInch,
    null);
    notifier = new Notifier(() -> {
        double currentTime = Timer.getTimestamp();
        double deltaTime = currentTime - lastSimTime;
        lastSimTime = currentTime;
        SimState.setSupplyVoltage(RobotController.getBatteryVoltage());
        physicsSim.setInputs(SimState.getMotorVoltage(), SimState.getMotorVoltage());
        physicsSim.update(deltaTime);SimState.setRawRotorPosition(physicsSim.getLeftPositionMeters());
        SimState.setRotorVelocity(physicsSim.getLeftVelocityMetersPerSecond());
    });
    notifier.startPeriodic(0.002);
}   

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
