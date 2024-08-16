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
import frc.lib.math.Conversions;
import frc.lib.util.SwerveModuleConstants;
import frc.robot.Constants;
import frc.robot.Robot;

public class SwerveModuleIOKrakenDFalconT implements SwerveModuleIO {
    public int moduleNumber;
    public SwerveModuleInputs m_inputs = new SwerveModuleInputs();
    private Rotation2d angleOffset;

    private TalonFX mAngleMotor;
    private TalonFX mDriveMotor;
    private CANcoder angleEncoder;

    private final SimpleMotorFeedforward driveFeedForward = new SimpleMotorFeedforward(Constants.Swerve.driveKS,
            Constants.Swerve.driveKV, Constants.Swerve.driveKA);

    /* drive motor control requests */
    private final DutyCycleOut driveDutyCycle = new DutyCycleOut(0);
    private final VelocityVoltage driveVelocity = new VelocityVoltage(0);

    /* angle motor control requests */
    private final PositionVoltage anglePosition = new PositionVoltage(0);

    public SwerveModuleIOKrakenDFalconT(int moduleNumber, SwerveModuleConstants moduleConstants) {
        this.moduleNumber = moduleNumber;
        this.angleOffset = moduleConstants.angleOffset;

        /* Angle Encoder Config */
        angleEncoder = new CANcoder(moduleConstants.cancoderID, Constants.Swerve.drivetrainCanBus);
        angleEncoder.getConfigurator().apply(Robot.ctreConfigs.swerveCANcoderConfig);

        /* Angle Motor Config */
        mAngleMotor = new TalonFX(moduleConstants.angleMotorID, Constants.Swerve.drivetrainCanBus);
        mAngleMotor.getConfigurator().apply(Robot.ctreConfigs.swerveAngleFXConfig);
        setTalonEncoderToAbsoluteValueWithOffset();

        /* Drive Motor Config */
        mDriveMotor = new TalonFX(moduleConstants.driveMotorID, Constants.Swerve.drivetrainCanBus);
        mDriveMotor.getConfigurator().apply(Robot.ctreConfigs.swerveDriveFXConfig);
        mDriveMotor.getConfigurator().setPosition(0.0);
    }

    public Rotation2d getCANcoderMeasuredHeading() {
        return Rotation2d.fromRotations(angleEncoder.getAbsolutePosition().getValue());
    }

    @Override
    public void setSwerveModule(SwerveModuleState desiredHeadingAndSpeed) {
        boolean isOpenLoop = false; // maybe we'll want this later

        desiredHeadingAndSpeed = SwerveModuleState.optimize(desiredHeadingAndSpeed, getState().angle);
        SmartDashboard.putNumber("rotaton_" + this.moduleNumber, desiredHeadingAndSpeed.angle.getRotations());
        mAngleMotor.setControl(anglePosition.withPosition(desiredHeadingAndSpeed.angle.getRotations()));
        setSpeed(desiredHeadingAndSpeed, isOpenLoop);
    }

    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
        if (isOpenLoop) {
            driveDutyCycle.Output = desiredState.speedMetersPerSecond / Constants.Swerve.maxSpeed;
            mDriveMotor.setControl(driveDutyCycle);
        } else {
            driveVelocity.Velocity = Conversions.MPSToRPS(desiredState.speedMetersPerSecond,
                    Constants.Swerve.wheelCircumference);
            driveVelocity.FeedForward = driveFeedForward.calculate(desiredState.speedMetersPerSecond);
            mDriveMotor.setControl(driveVelocity);
        }
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(
                Conversions.RPSToMPS(mDriveMotor.getVelocity().getValue(), Constants.Swerve.wheelCircumference),
                Rotation2d.fromRotations(mAngleMotor.getPosition().getValue()));
    }

    @Override
    public void readInputsFromHardware(SwerveModuleInputs inputs) {
        inputs.measuredHeadingAndSpeed.angle = getCANcoderMeasuredHeading();
        double velocityRotationsPerSecond = mDriveMotor.getVelocity().getValueAsDouble();
        inputs.measuredHeadingAndSpeed.speedMetersPerSecond = Conversions.RPSToMPS(
                velocityRotationsPerSecond, Constants.Swerve.wheelCircumference);

        inputs.measuredPosition = new SwerveModulePosition(
                Conversions.rotationsToMeters(mDriveMotor.getPosition().getValue(),
                        Constants.Swerve.wheelCircumference),
                Rotation2d.fromRotations(mAngleMotor.getPosition().getValue()));

    }

    @Override
    public void setTalonEncoderToAbsoluteValueWithOffset() {
        double absolutePosition = getCANcoderMeasuredHeading().getRotations() - angleOffset.getRotations();
        mAngleMotor.setPosition(absolutePosition);
    }

}
