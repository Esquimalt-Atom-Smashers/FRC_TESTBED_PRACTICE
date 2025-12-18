// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonSubsystem extends SubsystemBase {
  private final TalonSRX motor = new TalonSRX(1);
  private final Timer printTimer = new Timer();
  
  // Sensors
  private final DutyCycleEncoder absEncoder = new DutyCycleEncoder(0);
  private final Encoder relEncoder = new Encoder(1, 2);

  // PID Constants - Adjust these in your Constants file later
  private static final double P_POS = 3, I_POS = 0.0, D_POS = 0.0;
  private static final double P_VEL = 0.5, I_VEL = 0.0, D_VEL = 0.0;
  private static final double P_REL_POS = 0.003, I_REL_POS = 0.0, D_REL_POS = 0.00005;

  private static final double POS_TOLERANCE = 0.1; // How close to target is "done" (0.02 = 2%)
  private static final double REL_POS_TOLERANCE = 10; // How close to target is "done" (0.02 = 2%)

  // PID Controllers
  private final PIDController absPosController = new PIDController(P_POS, I_POS, D_POS);
  private final PIDController velController = new PIDController(P_VEL, I_VEL, D_VEL);
  private final PIDController relPosController = new PIDController(P_REL_POS, I_REL_POS, D_REL_POS);

  public TalonSubsystem() {
    motor.configFactoryDefault();
    motor.setInverted(false);
    printTimer.start();

    // Optional: If using Absolute encoder for PID, treat 0 and 1 as the same point
    absPosController.enableContinuousInput(0, 1);
    absPosController.setTolerance(POS_TOLERANCE);
    relPosController.setTolerance(REL_POS_TOLERANCE);
  }

  /**
   * GO TO POSITION AND HOLD
   * Uses the Absolute Encoder to reach a setpoint and stays there.
   */
  public Command goToAbsPositionHold(double targetRotation) {
    return run(() -> {
      double output = absPosController.calculate(absEncoder.get(), targetRotation);
      motor.set(ControlMode.PercentOutput, Math.min(0.05,Math.max(-0.05, output)));
    });
  }

  /**
   * GO TO POSITION AND STOP
   * Runs PID until the encoder is within tolerance, then stops the motor.
   */
  public Command goToAbsPositionAndStop(double targetRotation) {
    return run(() -> {
      double output = absPosController.calculate(absEncoder.get(), targetRotation);
      motor.set(ControlMode.PercentOutput, output);
    })
    .until(absPosController::atSetpoint)
    .finallyDo(interrupted -> motor.set(ControlMode.PercentOutput, 0));
  }
  /**
   * GO TO RELATIVE POSITION AND HOLD
   * Moves the motor a specific distance from its CURRENT position and stays there.
   */
  public Command goToRelativePositionHold(double offset) {
    // We use a variable to store the target calculated at the start
    final double[] target = new double[1]; 
    
    return run(() -> {
      double output = relPosController.calculate(relEncoder.get(), target[0]);
      motor.set(ControlMode.PercentOutput, output);
    })
    .beforeStarting(() -> {
      // This runs ONCE when the command starts
      target[0] = relEncoder.get() + offset;
    });
  }

  /**
   * GO TO RELATIVE POSITION AND HOLD BASED ON INITIAL POSITION
   * Moves the motor a specific distance from its INITIAL position and stays there.
   */
  public Command goToRelativePositionFromInitAndHold(double targetPosition) {
    return run(() -> {
      double output = relPosController.calculate(relEncoder.get(), targetPosition);
      motor.set(ControlMode.PercentOutput, Math.min(0.2,Math.max(-0.2, output)));
    
    });
  }
  

  /**
   * GO TO RELATIVE POSITION AND STOP
   * Moves the motor a specific distance from current position, then kills power.
   */
  public Command goToRelativePositionAndStop(double offset) {
    final double[] target = new double[1];//for some reason an array is needed to mutate in lambda so creating a single element array solves that

    return run(() -> {
      double output = relPosController.calculate(relEncoder.get(), target[0]);
      motor.set(ControlMode.PercentOutput, output);
    })
    .beforeStarting(() -> {
      target[0] = relEncoder.get() + offset;
      relPosController.setSetpoint(target[0]); // Ensure controller knows the goal
    })
    .until(absPosController::atSetpoint)
    .finallyDo(interrupted -> motor.set(ControlMode.PercentOutput, 0));
  }

  /**
   * RUN AT SPEED
   * Uses the Relative Encoder (Quadrature) to maintain a specific velocity.
   * targetVelocity is in units per second (as defined by your encoder setup).
   */
  public Command runAtSpeed(double targetVelocity) {
    return run(() -> {
      double output = velController.calculate(relEncoder.getRate(), targetVelocity);
      motor.set(ControlMode.PercentOutput, output);
    });
  }

  public Command setPowerCommand(double power) {
    return runOnce(() -> motor.set(ControlMode.PercentOutput, power));
  }

  

  @Override
  public void periodic() {
      if (printTimer.hasElapsed(1.0)) {
          System.out.println("Abs Pos: " + absEncoder.get());
          System.out.println("Rel Pos: " + relEncoder.get());
          System.out.println("Velocity: " + relEncoder.getRate());
          printTimer.reset();
      }
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}