// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonSubsystem extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  private final TalonSRX motor = new TalonSRX(1);
  private Timer printTimer = new Timer();
    
    // Initialize REV Through Bore Encoder on DIO Port 0
    // Using DutyCycleEncoder for Absolute Mode
  private final DutyCycleEncoder absEncoder = new DutyCycleEncoder(0);
  private final Encoder relEncoder = new Encoder(1, 2);

  public TalonSubsystem() {
    // Factory default the Talon to start from a known state
    motor.configFactoryDefault();
        
    // Optional: Invert motor if it spins the wrong way
    motor.setInverted(false);
    printTimer.start();
  }

  /**
   * sets the power of the motor from -1 to 1
   */
  public Command setPowerCommand(double power) {
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(() -> {motor.set(ControlMode.PercentOutput, power);});
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
    // Logic to print every 1 second
    if (printTimer.hasElapsed(1.0)) {
      // getAbsolutePosition() returns a value from 0.0 to 1.0
      double absPosition = absEncoder.get();
      System.out.println("Encoder Absolute Position: " + absPosition);
      double relPosition = relEncoder.get();
      double velocity = relEncoder.getRate();
      System.out.println("Encoder Relative Position: " + relPosition);
      System.out.println("Encoder AVelocity: " + velocity);
      printTimer.reset();
    }
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}