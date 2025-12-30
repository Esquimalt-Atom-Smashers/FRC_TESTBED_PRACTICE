package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.KRAKENMotorSubsystem;
import frc.robot.subsystems.NEOMotorSubsystem;
import frc.robot.subsystems.TalonBrushedSubsystem;

public class SameSpeedCommand extends ParallelCommandGroup{
    
    private final NEOMotorSubsystem neoMotorSubsystem;
    private final KRAKENMotorSubsystem krakenMotorSubsystem;
    private final TalonBrushedSubsystem talonMotorSubsystem;

    public SameSpeedCommand(
        //these are the parameters of the constructor
        NEOMotorSubsystem neoMotorSubsystem, 
        KRAKENMotorSubsystem krakenMotorSubsystem, 
        TalonBrushedSubsystem talonMotorSubsystem,
        double krakenVoltage,
        double neoVoltage,
        double talonVoltage)
        {
        this.neoMotorSubsystem = neoMotorSubsystem;
        this.krakenMotorSubsystem = krakenMotorSubsystem;
        this.talonMotorSubsystem = talonMotorSubsystem;
        
        //this.addRequirements(neoMotorSubsystem,talonMotorSubsystem,krakenMotorSubsystem);
        
        this.addCommands(
            neoMotorSubsystem.setNEOMotorVoltageCommand(neoVoltage),
            krakenMotorSubsystem.setMotorVoltageCommand(talonVoltage),
            talonMotorSubsystem.setPowerCommand(krakenVoltage));
        // this.addCommands(new InstantCommand(), new InstantCommand());
            
    }
    
}