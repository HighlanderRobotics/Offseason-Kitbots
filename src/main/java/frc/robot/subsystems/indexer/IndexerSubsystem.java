// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.indexer;

import java.util.function.DoubleSupplier;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.kinematics.Odometry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.subsystems.indexer.IndexerIO.IndexerIOInputs;

public class IndexerSubsystem extends SubsystemBase {
  /** Creates a new IndexerSubsystem. */
  IndexerIO io;
  IndexerIOInputsAutoLogged inputs = new IndexerIOInputsAutoLogged();

  public static final int TALON_ID = 0; // I might change this to become a config but idk. 
  // however I defintely need to change the ID when I figure that out


  VoltageOut voltageOut = new VoltageOut(0);
  TalonFX talon;

  public IndexerSubsystem(CANBus canbus) {
    talon = new TalonFX(TALON_ID,canbus);
    this.io = Robot.isReal() ? new IndexerIO(canbus) : new IndexerIOSim(canbus);
  }
  // I don't know if I need this lol
  private void setVoltage(double voltage) {
    io.setVoltage(voltage);
  }
  public Command setVoltageCommand(DoubleSupplier voltage) {
      return this.run(() -> io.setVoltage(voltage.getAsDouble()));
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    io.updateInputs(inputs);
    Logger.processInputs("Indexer", inputs);

    if (Robot.isSimulation()) {
      Logger.recordOutput("Indexer Voltage", talon.getMotorVoltage().getValueAsDouble());
    }
  }
}
