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
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerIO extends SubsystemBase {
  /** Creates a new IndexerIO. */


  public IndexerIO(int INDEXER_MOTOR_ID, CANBus canbus) {
      final TalonFX indexerMotor = new TalonFX(INDEXER_MOTOR_ID,canbus);
      indexerMotor.getConfigurator().apply(IndexerSubsystem.getIndexerConfig());

      indexerAngularVelocityRotationsPerSec = indexerMotor.getVelocity();
      indexerPositionRotations = indexerMotor.getPosition();
      indexerStatorCurrent = indexerMotor.getStatorCurrent();
      indexerSupplyCurrent = indexerMotor.getSupplyCurrent();
      indexerVoltage = indexerMotor.getMotorVoltage();
      indexerTemp = indexerMotor.getDeviceTemp();

      BaseStatusSignal.setUpdateFrequencyForAll(50.0,
         indexerAngularVelocityRotationsPerSec,indexerPositionRotations,indexerStatorCurrent,indexerSupplyCurrent,indexerVoltage,indexerTemp
      );
      indexerMotor.optimizeBusUtilization();
      
  }
  @AutoLog 
  public static class IndexerIOInputs {
    public double indexerAngularVelocityRotationsPerSec = 0.0;
    public double indexerPositionRotations = 0.0;
    public double indexerStatorCurrentAmp = 0.0;
    public double indexerSupplyCurrentAmp = 0.0;
    public double indexerVoltage = 0.0;
    public double indexerTempC = 0.0;
    public boolean indexerConnected = false;

  }

  //TODO: Add gear ratios

  protected TalonFX indexerMotor;

  private final VoltageOut voltageOut =
    new VoltageOut(0.0).withEnableFOC(true);
  
  private final StatusSignal<AngularVelocity> indexerAngularVelocityRotationsPerSec;
  private final StatusSignal<Angle> indexerPositionRotations;
  private final StatusSignal<Current> indexerStatorCurrent;
  private final StatusSignal<Current> indexerSupplyCurrent;
  private final StatusSignal<Voltage> indexerVoltage;
  private final StatusSignal<Temperature> indexerTemp;

  public void setVoltage(double voltage) {
    indexerMotor.setControl(voltageOut.withOutput(voltage));
  }
  public void updateInputs(IndexerIOInputs inputs) {
    BaseStatusSignal.refreshAll(
      indexerAngularVelocityRotationsPerSec, indexerPositionRotations,indexerStatorCurrent,indexerSupplyCurrent,indexerVoltage,indexerTemp
    );
    // set inputs
    inputs.indexerConnected =
        BaseStatusSignal.isAllGood(
            indexerAngularVelocityRotationsPerSec,
            indexerPositionRotations,
            indexerStatorCurrent,
            indexerSupplyCurrent,
            indexerVoltage,
            indexerTemp);
    inputs.indexerAngularVelocityRotationsPerSec = indexerAngularVelocityRotationsPerSec.getValueAsDouble();
    inputs.indexerPositionRotations = indexerPositionRotations.getValueAsDouble();
    inputs.indexerStatorCurrentAmp = indexerStatorCurrent.getValueAsDouble();
    inputs.indexerSupplyCurrentAmp = indexerSupplyCurrent.getValueAsDouble();
    inputs.indexerVoltage = indexerVoltage.getValueAsDouble();
    inputs.indexerTempC = indexerTemp.getValueAsDouble();
  }
}
