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
      leftIndexerMotor = new TalonFX(11,canbus);
       // TODO: add a config variable for the motor ids in IndexerSubsystem instead of hardcoding it
      indexerLeftVelocityRotsPerSec = leftIndexerMotor.getVelocity();
      indexerLeftPositionRots = leftIndexerMotor.getPosition();
      indexerLeftStatorCurrent = leftIndexerMotor.getStatorCurrent();
      indexerLeftSupplyCurrent = leftIndexerMotor.getSupplyCurrent();
      indexerLeftVoltage = leftIndexerMotor.getMotorVoltage();
      indexerLeftTemp = leftIndexerMotor.getDeviceTemp();

      rightIndexerMotor = new TalonFX(12,canbus);
      indexerRightVelocityRotsPerSec = rightIndexerMotor.getVelocity();
      indexerRightPositionRots = rightIndexerMotor.getPosition();
      indexerRightStatorCurrent = rightIndexerMotor.getStatorCurrent();
      indexerRightSupplyCurrent = rightIndexerMotor.getSupplyCurrent();
      indexerRightVoltage = rightIndexerMotor.getMotorVoltage();
      indexerRightTemp = rightIndexerMotor.getDeviceTemp();

      BaseStatusSignal.setUpdateFrequencyForAll(50.0,
         indexerLeftVelocityRotsPerSec,indexerLeftPositionRots,indexerLeftStatorCurrent,indexerLeftSupplyCurrent,indexerLeftVoltage,indexerLeftTemp,
         indexerRightVelocityRotsPerSec,indexerRightPositionRots,indexerRightStatorCurrent,
         indexerRightSupplyCurrent,indexerRightVoltage,indexerRightTemp
      );
      leftIndexerMotor.optimizeBusUtilization();
      rightIndexerMotor.optimizeBusUtilization();
      
  }
  @AutoLog
  public static class IndexerIOInputs {
    public double indexerLeftVelocityRotationsPerSec = 0.0;
    public double indexerLeftPositionRots = 0.0;
    public double indexerLeftStatorCurrentAmp = 0.0;
    public double indexerLeftSupplyCurrentAmp = 0.0;
    public double indexerLeftVoltage = 0.0;
    public double indexerLeftTempC = 0.0;
    // public boolean indexerLeftConnected = false;

    public double indexerRightVelocityRotationsPerSec = 0.0;
    public double indexerRightPositionRots = 0.0;
    public double indexerRightStatorCurrentAmp = 0.0;
    public double indexerRightSupplyCurrentAmp = 0.0;
    public double indexerRightVoltage = 0.0;
    public double indexerRightTempC = 0.0;
    // public boolean indexerRightConnected = false;

  }

  //TODO: Add gear ratios

  protected TalonFX leftIndexerMotor;
  protected TalonFX rightIndexerMotor;

  private final VoltageOut leftVoltage =
    new VoltageOut(0.0).withEnableFOC(true);
  private final VoltageOut rightVoltage =
    new VoltageOut(0.0).withEnableFOC(true);
  
  private final StatusSignal<AngularVelocity> indexerLeftVelocityRotsPerSec;
  private final StatusSignal<Angle> indexerLeftPositionRots;
  private final StatusSignal<Current> indexerLeftStatorCurrent;
  private final StatusSignal<Current> indexerLeftSupplyCurrent;
  private final StatusSignal<Voltage> indexerLeftVoltage;
  private final StatusSignal<Temperature> indexerLeftTemp;

  private final StatusSignal<AngularVelocity> indexerRightVelocityRotsPerSec;
  private final StatusSignal<Angle> indexerRightPositionRots;
  private final StatusSignal<Current> indexerRightStatorCurrent;
  private final StatusSignal<Current> indexerRightSupplyCurrent;
  private final StatusSignal<Voltage> indexerRightVoltage;
  private final StatusSignal<Temperature> indexerRightTemp;

  public void setLeftIndexerVoltage(double voltage) {
    leftIndexerMotor.setControl(leftVoltage.withOutput(voltage));
  }
  public void setRightIndexerVoltage(double voltage) {
    rightIndexerMotor.setControl(rightVoltage.withOutput(voltage));
  }
  public void updateInputs(IndexerIOInputs inputs) {
    BaseStatusSignal.refreshAll(
      indexerLeftVelocityRotsPerSec, indexerLeftPositionRots,indexerLeftStatorCurrent,indexerLeftSupplyCurrent,indexerLeftVoltage,indexerLeftTemp,
         indexerRightVelocityRotsPerSec,indexerRightPositionRots,indexerRightStatorCurrent,
         indexerRightSupplyCurrent,indexerRightVoltage,indexerRightTemp
    );
    // set inputs
    inputs.indexerLeftVelocityRotationsPerSec = indexerLeftVelocityRotsPerSec.getValueAsDouble();
    inputs.indexerLeftPositionRots = indexerLeftPositionRots.getValueAsDouble();
    inputs.indexerLeftStatorCurrentAmp = indexerLeftStatorCurrent.getValueAsDouble();
    inputs.indexerLeftSupplyCurrentAmp = indexerLeftSupplyCurrent.getValueAsDouble();
    inputs.indexerLeftVoltage = indexerLeftVoltage.getValueAsDouble();
    inputs.indexerLeftTempC = indexerLeftTemp.getValueAsDouble();
    
    inputs.indexerRightVelocityRotationsPerSec = indexerRightVelocityRotsPerSec.getValueAsDouble();
    inputs.indexerRightPositionRots = indexerRightPositionRots.getValueAsDouble();
    inputs.indexerRightStatorCurrentAmp = indexerRightStatorCurrent.getValueAsDouble();
    inputs.indexerRightSupplyCurrentAmp = indexerRightSupplyCurrent.getValueAsDouble();
    inputs.indexerRightVoltage = indexerRightVoltage.getValueAsDouble();
    inputs.indexerRightTempC = indexerRightTemp.getValueAsDouble();
  }
}
