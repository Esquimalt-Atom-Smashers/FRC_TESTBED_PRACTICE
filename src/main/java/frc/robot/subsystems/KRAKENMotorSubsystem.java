package frc.robot.subsystems;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkBase.ControlType;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class KRAKENMotorSubsystem extends SubsystemBase {
   
    private final TalonFX krakenMotor;
    private final Timer telemetryTimer = new Timer();

    public KRAKENMotorSubsystem() {
        krakenMotor = new TalonFX(3,"rio");

        TalonFXConfigurator krakenMotorConfig = krakenMotor.getConfigurator();

        TalonFXConfiguration krakenMotorConfigs = new TalonFXConfiguration();
        krakenMotorConfigs.Slot0.kP = 0.1;
        krakenMotorConfigs.Slot0.kI = 0;
        krakenMotorConfigs.Slot0.kD = 0;
        krakenMotorConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
        krakenMotorConfigs.CurrentLimits.StatorCurrentLimit = 4; // sets the max amps the motor can pull


        krakenMotorConfig.apply(krakenMotorConfigs);

        MotorOutputConfigs outputConfigs = new MotorOutputConfigs();

        krakenMotorConfig.apply(outputConfigs);
        krakenMotor.setPosition(0);
        telemetryTimer.reset();
        telemetryTimer.start();

    }
   

    public Command setMotorVoltageCommand(double voltage) {

        return runOnce (
          () -> {
            setTargetVoltage(voltage);
          });
      }
    
    

    public Command setMotorPositionCommand(double position) {
        return runOnce (
            () -> {
                setTargetPosition(position);
            });
    }
    public void setTargetPosition(double Position) {
    krakenMotor.setControl(new PositionVoltage(Position).withSlot(0));
    System.out.println("Setting position to: "+ Position);
    }


  public void setTargetVoltage(double Voltage) {
    krakenMotor.setControl(new VoltageOut(Voltage));
    System.out.println("Setting voltage to: " + Voltage);
  }

  public void disableMotor() {
    setTargetVoltage(0);
  }
}

