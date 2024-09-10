package frc.robot.subsystems;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.SwerveModuleIO.SwerveModuleInputs;

public class SwerveModule extends SubsystemBase {

    SwerveModuleIO m_io;
    SwerveModuleInputs m_inputs = new SwerveModuleInputs();
    public int m_moduleNumber;

    public SwerveModule(SwerveModuleIO io, int moduleNumber) {
        this.m_io = io;
        this.m_moduleNumber = moduleNumber;

        m_inputs.measuredPosition = new SwerveModulePosition();
        m_inputs.measuredHeadingAndSpeed = new SwerveModuleState();
    }

    @Override
    public void periodic() {
        this.m_io.readInputsFromHardware(m_inputs);
    }

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
        // desiredState = SwerveModuleState.optimize(desiredState, getState().angle);
        // SmartDashboard.putNumber("rotaton_"+this.moduleNumber,
        // desiredState.angle.getRotations());
        // mAngleMotor.setControl(anglePosition.withPosition(desiredState.angle.getRotations()));
        // setSpeed(desiredState, isOpenLoop);
        m_io.setSwerveModule(desiredState);
    }

    public SwerveModuleState getState() {
        return m_inputs.measuredHeadingAndSpeed;
    }

    public SwerveModulePosition getPosition() {
        return m_inputs.measuredPosition;
    }

    public void updateTalonEncoderToAbsoluteValueWithOffsert() {
        m_io.setTalonEncoderToAbsoluteValueWithOffset();
    }

}