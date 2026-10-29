// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.flywheel;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.units.measure.*; // im lazy
import static edu.wpi.first.units.Units.*; // still lazy

/* 
Things to log:
Voltage
Tempature (C)
current amps (supply)
current amps (stator)
Velocity (r/s)
position of flywheel (initial) (rotations)

*/

// this code will not build until the drum subsystem is finished

public class FlywheelIO {
  @AutoLog 
  public static class FlywheelIOInputs{
    public double velocityRotPerSec = 0.0;
    public boolean connected = false;
    public double voltage = 0.0;
    public double statorCurrentAmps = 0.0;
    public double supplyCurrentAmps = 0.0;
    public double tempCelsius = 0.0;
    public double flywheelPositionRotations = 0.0;
  }

  protected final TalonFX leader;

  private StatusSignal<AngularVelocity> velocity;
  private StatusSignal<Voltage> voltage;
  private StatusSignal<Current> supplyCurrent;
  private StatusSignal<Current> statorCurrent;
  private StatusSignal<Temperature> temp;
  private StatusSignal<Angle> flywheelPosition;

  private VoltageOut voltageOut = new VoltageOut(0.0).withEnableFOC(true); // start at 0 volts
  private MotionMagicVelocityVoltage motionMagic = new MotionMagicVelocityVoltage(0.0).withEnableFOC(true);

  private double velocitySetpointRotPerSecond = Double.NaN; // used for logging

  public FlywheelIO(CANBus canBus) {
    leader = new TalonFX(0, canBus); // todo: get actual device id
    leader.getConfigurator().apply(FlywheelSubsystem.getFlywheelConfig());
    velocity = leader.getVelocity();
    voltage = leader.getMotorVoltage();
    statorCurrent = leader.getStatorCurrent();
    supplyCurrent = leader.getSupplyCurrent();
    temp = leader.getDeviceTemp();
    flywheelPosition = leader.getPosition();

    BaseStatusSignal.setUpdateFrequencyForAll(50.0, velocity, voltage, statorCurrent, supplyCurrent, temp, flywheelPosition);
    leader.optimizeBusUtilization();
  }

  public void updateInputs(FlywheelIOInputs inputs) {
    BaseStatusSignal.refreshAll(velocity, voltage, statorCurrent, supplyCurrent, temp, flywheelPosition);
    inputs.connected = BaseStatusSignal.isAllGood(velocity, voltage, statorCurrent, supplyCurrent, temp, flywheelPosition);
    inputs.flywheelPositionRotations = flywheelPosition.getValue().in(Rotation);
    inputs.velocityRotPerSec = velocity.getValue().in(Rotation.per(Second));
    inputs.voltage = voltage.getValueAsDouble();
    inputs.statorCurrentAmps = statorCurrent.getValueAsDouble();
    inputs.supplyCurrentAmps = supplyCurrent.getValueAsDouble();
    inputs.tempCelsius = temp.getValue().in(Celsius);
  }

  public void setVoltage(double voltage) {
    velocitySetpointRotPerSecond = Double.NaN;
    leader.setControl(voltageOut.withOutput(voltage));
  }

  public void setVelocitySetpoint(double velocityRotPerSec) {
    velocitySetpointRotPerSecond = velocityRotPerSec;
    leader.setControl(motionMagic.withVelocity(velocityRotPerSec));
  }

  public double getSetpointRotPerSecond() {
    return velocitySetpointRotPerSecond;
  }
}