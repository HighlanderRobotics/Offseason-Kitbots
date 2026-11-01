// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RollerIO extends SubsystemBase {
  @AutoLog
    public static class DrivetrainIOInputs {
        public double OutputVolts = 0.0;
        public double VelocityMetersPerSecond = 0.0;
        public double PositionMeters = 0.0;
        public double CurrentAmps = 0.0;
        public double TempCelsius = 0.0;
    }

    public void updateInputs(DrivetrainIOInputs inputs) {
    BaseStatusSignal.refreshAll(
        AppliedVoltage, 
        AngularVelocityRPS, 
        PositionMeters, 
        SupplyCurrent, 
        TempCelsius 
        );

    inputs.OutputVolts = AppliedVoltage.getValueAsDouble();
    inputs.VelocityMetersPerSecond = AngularVelocityRPS.getValueAsDouble();
    inputs.PositionMeters = PositionMeters.getValueAsDouble();
    inputs.CurrentAmps = SupplyCurrent.getValueAsDouble();
    inputs.TempCelsius = TempCelsius.getValueAsDouble();

}
    TalonFX leftTalon = new TalonFX(IntakeSubsystem.LEFT_TALON_ID);
    TalonFX rightTalon = new TalonFX(IntakeSubsystem.RIGHT_TALON_ID);

    VoltageOut leftVoltage = new VoltageOut(0);
    VoltageOut rightVoltage = new VoltageOut(0);
    public void setVolts(double value) {
        leftTalon.setControl(leftVoltage.withOutput(value));
    }
    private final StatusSignal<AngularVelocity> AngularVelocityRPS = leftTalon.getVelocity();
    private final StatusSignal<Voltage> AppliedVoltage = leftTalon.getMotorVoltage();
    private final StatusSignal<Angle> PositionMeters = leftTalon.getPosition();
    private final StatusSignal<Current> SupplyCurrent = leftTalon.getSupplyCurrent();
    private final StatusSignal<Temperature> TempCelsius = leftTalon.getDeviceTemp();

    public RollerIO(CANBus canbus) {
    BaseStatusSignal.setUpdateFrequencyForAll(
        50.0,
        AppliedVoltage, 
        AngularVelocityRPS, 
        PositionMeters, 
        SupplyCurrent, 
        TempCelsius);
    leftTalon.optimizeBusUtilization();
    rightTalon.optimizeBusUtilization();
    }
  public RollerIO() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
