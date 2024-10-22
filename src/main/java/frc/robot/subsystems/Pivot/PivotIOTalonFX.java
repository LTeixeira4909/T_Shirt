package frc.robot.subsystems.Pivot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class PivotIOTalonFX implements PivotIO{
    private final TalonFX m_pivotLeader;// m_pivotFollower;

    public PivotIOTalonFX() {
        m_pivotLeader = new TalonFX(21);
       // m_pivotFollower = new TalonFX(22);

        final TalonFXConfiguration pivotMotorConfig = new TalonFXConfiguration();
        pivotMotorConfig.CurrentLimits.SupplyCurrentLimit = 40.0;
        pivotMotorConfig.CurrentLimits.SupplyCurrentLimitEnable = true;

        m_pivotLeader.getConfigurator().apply(pivotMotorConfig);
       // m_pivotFollower.getConfigurator().apply(pivotMotorConfig);

        //m_pivotFollower.setControl(new Follower(m_pivotLeader.getDeviceID(), false));
    }

    @Override
    public void updateInputs(PivotIOInputs inputs) {
        inputs.pivotPositionRot = m_pivotLeader.getPosition().getValueAsDouble();
    }

    @Override
    public void setSpeed(double speed) {
        m_pivotLeader.setControl(new DutyCycleOut(speed));
    }

    @Override
    public void setBrakeMode(boolean enableBrakeMode) {
        final NeutralModeValue neutralModeValue = enableBrakeMode ? NeutralModeValue.Brake : NeutralModeValue.Coast;
        m_pivotLeader.setNeutralMode(neutralModeValue);
        //m_pivotFollower.setNeutralMode(neutralModeValue);
    }
}
