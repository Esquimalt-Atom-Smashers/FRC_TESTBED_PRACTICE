// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase.ControlType;

public class MotorSubsystem extends SubsystemBase {

  /** Creates a new ExampleSubsystem. */
  private SparkMax neoMotor;
  private SparkClosedLoopController neoMotorController;
  public MotorSubsystem() {
    neoMotor = new SparkMax(3, MotorType.kBrushless);
    SparkMaxConfig neoMotorConfig = new SparkMaxConfig();
    neoMotorController = neoMotor.getClosedLoopController();

    neoMotorConfig.closedLoop.pid(0.1, 0, 0);
    neoMotorConfig.smartCurrentLimit(1, 8, 50);

    neoMotor.configure(neoMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    neoMotorController.setReference(0, SparkMax.ControlType.kVoltage);
  }


  public Command runNeoMotorVoltageCommand() {

    return runOnce (
      () -> {
        setTargetVoltage(1);
      });
  }
  
  public Command runNeoMotorVelocityCommand() {
    return runOnce(
      () -> {
        setTargetVelocity(0.2);
      });
  }

  public Command runNeoMotorPositionCommand(double motorPosition) {
    return runOnce(
      () -> {
        setTargetPosition(motorPosition);
      });
  }
  
  public void setTargetVelocity(double velocity) {
    neoMotorController.setReference(velocity, ControlType.kVelocity);
    System.out.println("Setting velocity to: "+ velocity);
  }
  public void setTargetPosition(double position) {
    neoMotorController.setReference(position, ControlType.kPosition);
    System.out.println("Setting position to: "+ position);
  }


  public void setTargetVoltage(double voltage) {
    neoMotorController.setReference(voltage, ControlType.kVoltage);
    System.out.println("Setting voltage to: " + voltage);
  }


  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          /* one-time action goes here */
        });
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
