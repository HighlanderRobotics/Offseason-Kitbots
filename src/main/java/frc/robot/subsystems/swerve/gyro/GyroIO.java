package frc.robot.subsystems.swerve.gyro;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.littletonrobotics.junction.AutoLog;

public interface GyroIO {

  @AutoLog
  public static class GyroIOInputs extends SubsystemBase {
    public Rotation2d pitch = new Rotation2d();
    public Rotation2d roll = new Rotation2d();
    public Rotation2d yaw = new Rotation2d();
    public double yawVelocityRadPerSec = 0.0;


    public boolean isConnected = false;
  }

  public void updateInputs(GyroIOInputs inputs);

  /**
   * Resets the gyro's position
   *
   * @param yaw the position to set the gyro to
   */
  public void setYaw(Rotation2d yaw);
}