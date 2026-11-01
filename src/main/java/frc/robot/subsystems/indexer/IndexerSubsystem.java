// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.indexer;

import java.util.function.DoubleSupplier;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.kinematics.Odometry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerSubsystem extends SubsystemBase {
  /** Creates a new IndexerSubsystem. */
  public static TalonFXConfiguration getIndexerConfig(){
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    config.MotorOutput.NeutralMode = NeutralModeValue.Coast;

    config.Feedback.SensorToMechanismRatio = 1.0; // TODO: add gear ratio

    config.CurrentLimits.StatorCurrentLimit = 30.0; // Idk if its good its in amps
    config.CurrentLimits.StatorCurrentLimitEnable = true;

    config.CurrentLimits.SupplyCurrentLimit = 30.0; // Idk if its good its in amps
    config.CurrentLimits.SupplyCurrentLimitEnable = true;

    // TODO: add PID values
    config.Slot0.kA = 0.0;
    config.Slot0.kP = 0.0;
    config.Slot0.kI = 0.0;
    config.Slot0.kD = 0.0;
    config.Slot0.kS = 0.0;
    config.Slot0.kV = 0.0;
    
    return config;
  }
  IndexerIO io;
  IndexerIOInputsAutoLogged inputs = new IndexerIOInputsAutoLogged();

  public static final int INDEXER_MOTOR_ID = 11;


  VoltageOut voltageOut = new VoltageOut(0);
  TalonFX talon;

  public IndexerSubsystem(CANBus canbus) {
    talon = new TalonFX(INDEXER_MOTOR_ID,canbus);
    this.io = Robot.isReal() ? new IndexerIO(INDEXER_MOTOR_ID, canbus) : new IndexerIOSim(INDEXER_MOTOR_ID, canbus);
  }
  // I don't know if I need this lol
  private void setVoltage(double voltage) {
    io.setVoltage(voltage);
  }
  public Command setVoltageCommand(DoubleSupplier voltage) {
      return this.run(() -> io.setVoltage(voltage.getAsDouble()));
  }
  public Command stopIndexer() {
    return this.run(() -> io.setVoltage(0));
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
