package frc.robot;

import javax.xml.xpath.XPathConstants;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PS4Controller.Axis;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.autos.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.Pivot.Pivot;
import frc.robot.subsystems.Pivot.PivotIO;
import frc.robot.subsystems.Pivot.PivotIOTalonFX;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */
    private final Joystick driver = new Joystick(0);

    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);

    private final JoystickButton pivotUp = new JoystickButton(driver, XboxController.Button.kA.value);
    private final JoystickButton pivotDown = new JoystickButton(driver, XboxController.Button.kB.value);
    private final JoystickButton stopButton = new JoystickButton(driver, XboxController.Button.kX.value);

    private final JoystickButton shoot = new JoystickButton(driver, XboxController.Button.kRightBumper.value);

    private final JoystickButton close = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
    /* Subsystems */
    private final Swerve s_Swerve = new Swerve();
    private final Pivot s_pivot;
    private final Shooter s_Shooter;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        s_Swerve.setDefaultCommand(
                new TeleopSwerve(
                        s_Swerve,
                        () -> -driver.getRawAxis(translationAxis),
                        () -> -driver.getRawAxis(strafeAxis),
                        () -> -driver.getRawAxis(rotationAxis),
                        () -> robotCentric.getAsBoolean()));
        s_pivot = new Pivot(new PivotIOTalonFX());
        s_Shooter = new Shooter();
        // Configure the button bindings
        configureButtonBindings();
    }

    public double DriverRawAxis() {
        return driver.getRawAxis(4);

    } 

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
     * it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /* Driver Buttons */
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroHeading()));
        pivotUp.whileTrue(s_pivot.pivotUp());
        pivotDown.whileTrue(s_pivot.pivotDown());
        shoot.onTrue(s_Shooter.shoot());
        close.onTrue(s_Shooter.close());
        stopButton.onTrue(s_pivot.idle());
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return new exampleAuto(s_Swerve);
    } 
}
