// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.indexer;

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

public class IndexerIO {
  /** Creates a new IndexerIO. */


  public IndexerIO(CANBus canbus) {
      indexerMotor = new TalonFX(11,canbus);
       // TODO: add a config variable for the motor ids in IndexerSubsystem instead of hardcoding it
      indexerVelocityRotsPerSec = indexerMotor.getVelocity();
      indexerPositionRots = indexerMotor.getPosition();
      indexerStatorCurrent = indexerMotor.getStatorCurrent();
      indexerSupplyCurrent = indexerMotor.getSupplyCurrent();
      indexerVoltage = indexerMotor.getMotorVoltage();
      indexerTemp = indexerMotor.getDeviceTemp();

      BaseStatusSignal.setUpdateFrequencyForAll(50.0,
         indexerVelocityRotsPerSec,indexerPositionRots,indexerStatorCurrent,indexerSupplyCurrent,indexerVoltage
      );
      indexerMotor.optimizeBusUtilization();
      
  }
  @AutoLog 
  public static class IndexerIOInputs {
    public double indexerVelocityRotationsPerSec = 0.0;
    public double indexerPositionRots = 0.0;
    public double indexerStatorCurrentAmp = 0.0;
    public double indexerSupplyCurrentAmp = 0.0;
    public double indexerVoltage = 0.0;
    public double indexerTempC = 0.0;
    // public boolean indexerConnected = false;

  }

  //TODO: Add gear ratios

  protected TalonFX indexerMotor;

  private final VoltageOut voltageOut =
    new VoltageOut(0.0).withEnableFOC(true);
  
  private final StatusSignal<AngularVelocity> indexerVelocityRotsPerSec;
  private final StatusSignal<Angle> indexerPositionRots;
  private final StatusSignal<Current> indexerStatorCurrent;
  private final StatusSignal<Current> indexerSupplyCurrent;
  private final StatusSignal<Voltage> indexerVoltage;
  private final StatusSignal<Temperature> indexerTemp;

  public void setVoltage(double voltage) {
    indexerMotor.setControl(voltageOut.withOutput(voltage));
  }
  public void updateInputs(IndexerIOInputs inputs) {
    BaseStatusSignal.refreshAll(
      indexerVelocityRotsPerSec, indexerPositionRots,indexerStatorCurrent,indexerSupplyCurrent,indexerVoltage,indexerTemp
    );
    // set inputs
    inputs.indexerVelocityRotationsPerSec = indexerVelocityRotsPerSec.getValueAsDouble();
    inputs.indexerPositionRots = indexerPositionRots.getValueAsDouble();
    inputs.indexerStatorCurrentAmp = indexerStatorCurrent.getValueAsDouble();
    inputs.indexerSupplyCurrentAmp = indexerSupplyCurrent.getValueAsDouble();
    inputs.indexerVoltage = indexerVoltage.getValueAsDouble();
    inputs.indexerTempC = indexerTemp.getValueAsDouble();
  }
}
