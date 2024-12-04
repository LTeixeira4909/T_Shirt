// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */
    //  Solenoid m_soleniod = new Solenoid(0, PneumaticsModuleType.CTREPCM, 0);
  PWMSparkMax m_soleniod = new PWMSparkMax(6);
  Solenoid m_uselessSoleniod = new Solenoid(PneumaticsModuleType.CTREPCM, 0);
  
  public Shooter() {
    setDefaultCommand(idle());
  }

  @Override
  public void periodic() {
   }
    public Command shoot(){
      System.out.println("open");
      return this.run(() -> m_soleniod.set(1));
    }
  
    public Command idle(){
      System.out.println("idle");
      return this.run(() -> m_soleniod.set(0));
    }

   public Command close(){
      System.out.println("close");
      return this.run(() -> m_soleniod.set(0));
    }
}

