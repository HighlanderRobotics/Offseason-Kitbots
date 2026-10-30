package frc.robot.subsystems.swerve.gyro;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;

public class GyroIOReal implements GyroIO {

  private final Pigeon2 pigeon;

  private final StatusSignal<Angle> yaw;
  private final StatusSignal<Angle> pitch;
  private final StatusSignal<Angle> roll;
  private final StatusSignal<AngularVelocity> yawVelocity;

  public GyroIOReal(int id, Pigeon2Configuration config, CANBus canbus) {
    pigeon = new Pigeon2(id, canbus);

    yaw = pigeon.getYaw();
    pitch = pigeon.getPitch();
    roll = pigeon.getRoll();
    yawVelocity = pigeon.getAngularVelocityZWorld();
    pigeon.getConfigurator().apply(config);

    pigeon.optimizeBusUtilization();
  }

  @Override
  public void updateInputs(GyroIOInputs inputs) {
    BaseStatusSignal.refreshAll(yaw, pitch, roll, yawVelocity);

    inputs.isConnected = BaseStatusSignal.isAllGood(yaw, pitch, roll, yawVelocity);
    inputs.yaw = new Rotation2d(yaw.getValue());
    inputs.pitch = new Rotation2d(pitch.getValue());
    inputs.roll = new Rotation2d(roll.getValue());
    inputs.yawVelocityRadPerSec = yawVelocity.getValue().in(Units.RadiansPerSecond);
  }

  @Override
  public void setYaw(Rotation2d yaw) {
    pigeon.setYaw(yaw.getDegrees());
  }
}