/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2485.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2485.robot.commands.ExampleCommand;
import org.usfirst.frc.team2485.robot.subsystems.ExampleSubsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	public static ExampleSubsystem m_subsystem = new ExampleSubsystem();
	public static OI m_oi;

	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();
	
	public static TalonSRX talonL = new TalonSRX(1);
	public static TalonSRXWrapper talonLeft = new TalonSRXWrapper(ControlMode.PercentOutput, talonL);
	public static TalonSRX talonR = new TalonSRX(2);
	public static TalonSRXWrapper talonRight = new TalonSRXWrapper(ControlMode.PercentOutput, talonR);
	public static VictorSPX v1left = new VictorSPX(3);
	public static VictorSPX v2left = new VictorSPX(4);
	public static VictorSPX v3left = new VictorSPX(5);
	public static VictorSPX v1right = new VictorSPX(6);
	public static VictorSPX v2right = new VictorSPX(7);
	public static VictorSPX v3right = new VictorSPX(8);
	public Joystick j = new Joystick(0);
	public SpeedControllerWrapper leftMotors = new SpeedControllerWrapper(talonLeft);
	public SpeedControllerWrapper rightMotors = new SpeedControllerWrapper(talonRight);


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_oi = new OI();
		m_chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", m_chooser);
		v1left.follow(talonL);
		v2left.follow(talonL);
		v3left.follow(talonL);
		v1right.follow(talonR);
		v2right.follow(talonR);
		v3right.follow(talonR);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		m_autonomousCommand = m_chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		double throttle = ThresholdHandler.deadbandAndScale(j.getRawAxis(1), .2, 0, 1);
		double steering = ThresholdHandler.deadbandAndScale(j.getRawAxis(4), .2, 0, 1);
		double leftPWM = throttle+steering;
		double rightPWM = throttle-steering;
		if(leftPWM > 1) {
			rightPWM /= leftPWM;
			leftPWM /= leftPWM;
		}
		if(rightPWM > 1) {
			leftPWM /= rightPWM;
			rightPWM /= rightPWM;
		}
		leftMotors.set(leftPWM);
		rightMotors.set(rightPWM);
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
