package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;

public interface IMotorSubsystem {
    public Command setMotorVoltageCommand(double voltage);
    public Command setMotorPositonCommand(double position);
    
}
