package frc.robot.subsystems;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.math.Conversions;
import frc.lib.util.SwerveModuleConstants;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.SwerveModuleIO.SwerveModuleInputs;
import frc.robot.Constants.Swerve;

public class SwerveModule extends SubsystemBase {
    
    SwerveModuleIO m_io;
    SwerveModuleInputs m_inputs;
    public int moduleNumber;

    public SwerveModule(SwerveModuleIO io, int moduleNumber) {
        this.m_io = io;
    }

    @Override
    public void periodic() {
        this.m_io.readInputsFromHardware(m_inputs);
    }
    

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop){
        // desiredState = SwerveModuleState.optimize(desiredState, getState().angle); 
        // SmartDashboard.putNumber("rotaton_"+this.moduleNumber, desiredState.angle.getRotations());
        // mAngleMotor.setControl(anglePosition.withPosition(desiredState.angle.getRotations()));
        // setSpeed(desiredState, isOpenLoop);
        m_io.setSwerveModule(desiredState);
    }

    

    public Rotation2d getCANcoder(){
        
        return m_inputs.measuredHeadingAndSpeed.angle; // Rotation2d.fromRotations(angleEncoder.getAbsolutePosition().getValue());
    }

    public void resetToAbsolute(){
        double absolutePosition = getCANcoder().getRotations() - angleOffset.getRotations();
        mAngleMotor.setPosition(absolutePosition);
    }

    public SwerveModuleState getState(){
        return new SwerveModuleState(
            Conversions.RPSToMPS(mDriveMotor.getVelocity().getValue(), Constants.Swerve.wheelCircumference), 
            Rotation2d.fromRotations(mAngleMotor.getPosition().getValue())
        );
    }

    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(
            Conversions.rotationsToMeters(mDriveMotor.getPosition().getValue(), Constants.Swerve.wheelCircumference), 
            Rotation2d.fromRotations(mAngleMotor.getPosition().getValue())
        );
    }
    
}