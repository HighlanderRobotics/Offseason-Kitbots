package frc.robot.subsystems.swerve.constants;

import static edu.wpi.first.units.Units.FeetPerSecond;
import static edu.wpi.first.units.Units.MetersPerSecondPerSecond;
import static edu.wpi.first.units.Units.Pounds;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.sim.TalonFXSimState.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Mass;
import frc.robot.subsystems.swerve.module.Module.ModuleConstants;

public class KitbotSwerveConstants extends SwerveConstants {

    @Override
    public String getName() {
       return "Kitbot";
    }

    @Override
    public double getTrackWidthX() {
        return Units.inchesToMeters(21.75);
    }

    @Override
    public double getTrackWidthY() {
        return Units.inchesToMeters(21.75);

    }

    @Override
    public double getBumperWidth() {
        return Units.inchesToMeters(27.00);
    }

    @Override
    public double getBumperLength() {
         return Units.inchesToMeters(27.00);
    }

    @Override
    public double getMaxLinearSpeed() {
     return Units.feetToMeters(16.8); 
    }

    @Override
    public double getMaxLinearAcceleration() {
        return 14.6;
    // This is not an exact value
    }

    @Override
    public double getDriveGearRatio() {
        return 7.03;
    }

    @Override
    public double getTurnGearRatio() {
        return 5.27;
        // got this from https://www.swervedrivespecialties.com/products/mk5n-swerve-module
    }

    @Override
    public Mass getMass() {
        // 16 lb battery, 15 lb bumpers (might change)
        return Pounds.of(77.186 + 16.0 + 15.0);
    }


 @Override
  public ModuleConstants getFrontLeftModuleConstants() {
    return new ModuleConstants(
        0, "Front Left", 0, 1, 0, Rotation2d.fromRotations(-0.29).plus(Rotation2d.k180deg));
  }

  @Override
  public ModuleConstants getFrontRightModuleConstants() {
    return new ModuleConstants(1, "Front Right", 2, 3, 1, Rotation2d.fromRotations(0.012));
  }

  @Override
  public ModuleConstants getBackLeftModuleConstants() {
    return new ModuleConstants(
        2, "Back Left", 4, 5, 2, Rotation2d.fromRotations(0.229).plus(Rotation2d.k180deg));
  }

  @Override
  public ModuleConstants getBackRightModuleConstants() {
    return new ModuleConstants(3, "Back Right", 6, 7, 3, Rotation2d.fromRotations(-0.205));
  }



    @Override
    public int getGyroID() {
return 0;
    }

    @Override
    public Pigeon2Configuration getGyroConfig() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGyroConfig'");
    }

    @Override
    public TalonFXConfiguration getDriveConfig() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDriveConfig'");
    }

    @Override
    public TalonFXConfiguration getTurnConfig(int cancoderID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTurnConfig'");
    }

    @Override
    public MotorType getTurnMotorType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTurnMotorType'");
    }

    @Override
    public MotorType getDriveMotorType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDriveMotorType'");
    }

    @Override
    public CANcoderConfiguration getCancoderConfig(Rotation2d cancoderOffset) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCancoderConfig'");
    }

    @Override
    public double getHeadingVelocityKP() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHeadingVelocityKP'");
    }

}
