package frc.robot.subsystems.Pivot;

import org.littletonrobotics.junction.AutoLog;

public interface PivotIO {

    @AutoLog
    public static class PivotIOInputs{
        public double pivotPositionRot = 0.0;

    }

    public default void updateInputs(PivotIOInputs inputs){}

    public default void setSpeed(double speed) {}

    public default void setBrakeMode(boolean enableBrakeMode) {}

}
