// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.swerve.gyro;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import java.util.Optional;
public class GyroIOReal implements GyroIO{
  /** Creates a new GyroIOReal. */

private final Pigeon2 pigeon;

  private final StatusSignal<Angle> yaw;
  private final StatusSignal<Angle> pitch;
  private final StatusSignal<Angle> roll;
  private final StatusSignal<AngularVelocity> yawVelocity;

  public GyroIOReal(int id, CANBus canbus) {
    pigeon = new Pigeon2(id, canbus);
    yaw = pigeon.getYaw();
    yawVelocity= pigeon.getAngularVelocityZWorld();
    pitch = pigeon.getPitch();
    roll = pigeon.getRoll();
  }
@Override
public void updateInputs(GyroIOInputs inputs) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'updateInputs'");
}
@Override
public void setYaw(Rotation2d yaw) {
  // TODO Auto-generated method stub
  throw new UnsupportedOperationException("Unimplemented method 'setYaw'");
}}
