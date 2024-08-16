package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public interface SwerveModuleIO {

  @AutoLog
  public static class SwerveModuleInputs {
    public SwerveModuleState measuredHeadingAndSpeed;
    public SwerveModulePosition measuredPosition;
  }

  public void readInputsFromHardware(SwerveModuleInputs inputs);

  public void setSwerveModule(SwerveModuleState desiredHeadingAndSpeed);

  public void setTalonEncoderToAbsoluteValueWithOffset();
}
