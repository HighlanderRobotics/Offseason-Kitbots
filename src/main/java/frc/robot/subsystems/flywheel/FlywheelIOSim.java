package frc.robot.subsystems.flywheel;

package frc.robot.subsystems.flywheel;

public class FlywheelIOSim extends FlywheelIO {

public class FlywheelIOSim extends SubsystemBase {
  /** Creates a new FlywheelIOSim. */
  public FlywheelIOSim() {
  }

  // sim timing instance variables
  private double lastSimTime = 0.0;
  private Notifier notifier;
  
  // need to create some kind of flywheelSim, but what would a linear system have to do with anything?
  private LinearSystem<N1,N1,N1> flywheelMotorLinearSystem = LinearSystemId.createFlywheelSystem(flywheelMechanismMotor, 1, 10);
  private FlywheelSim wheelSim = new FlywheelSim(flywheelMotorLinearSystem, flywheelMechanismMotor, null); 

  public FlywheelIOSim(CANBus canBus) {
    super(canBus);
    
    leaderTalonSimState = leader.getSimState();

    notifier = new Notifier(() -> {
      
      //calculate delta
      double currentTime = Timer.getTimestamp();
      double deltaTime = currentTime - lastSimTime;
      lastSimTime = currentTime;

      leaderTalonSimState.setSupplyVoltage(RobotController.getBatteryVoltage());
      wheelSim.setInputVoltage(leaderTalonSimState.getMotorVoltage());
      wheelSim.update(deltaTime);

      // wait i set the velocity to the acceleration oops
      wheelSim.setAngularVelocity(wheelSim.getAngularVelocityRPM());
    });
    notifier.startPeriodic(0.002);
  } 
}