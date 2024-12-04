package frc.robot.subsystems.Pivot;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pivot extends SubsystemBase{

    private final PivotIO m_io;
    private final PivotIOInputsAutoLogged m_inputs = new PivotIOInputsAutoLogged();

    public Pivot(PivotIO io){
        m_io = io;
        // setDefaultCommand(this.idle());
        
    }

    @Override
    public void periodic() {
        m_io.updateInputs(m_inputs);
        Logger.processInputs("PivotInputs", m_inputs);
        // System.out.println(this.getCurrentCommand() != null ? this.getCurrentCommand().getName() : "NULL");
    }

    public Command pivotUp(){
        return this.run(() -> m_io.setSpeed(0.1));

    }

    public Command pivotDown(){
        return this.run(() -> m_io.setSpeed(-0.1));
    }

    public Command idle() {
        return this.run(() -> m_io.setSpeed(0.0));
    }
}
