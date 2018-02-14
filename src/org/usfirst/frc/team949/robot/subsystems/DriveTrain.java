package org.usfirst.frc.team949.robot.subsystems;

import org.usfirst.frc.team949.robot.RobotMap;
import org.usfirst.frc.team949.robot.commands.JoyStickDrive;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 *
 */
public class DriveTrain extends Subsystem {

	private DifferentialDrive drive;

	private final ADXRS450_Gyro g;

	private SpeedControllerGroup r;
	private SpeedControllerGroup l;
	private WPI_TalonSRX r0, r1, l0, l1;

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new JoyStickDrive());
	}

	public DriveTrain() {
		// Christoff contributed all of the "this." code in this class
		this.g = new ADXRS450_Gyro();
		gyroCalibrate();

		this.r0 = new WPI_TalonSRX(RobotMap.rigthMaster);
		this.r1 = new WPI_TalonSRX(RobotMap.rightSlave);
		this.l0 = new WPI_TalonSRX(RobotMap.leftMaster);
		this.l1 = new WPI_TalonSRX(RobotMap.leftSlave);

		setUpEncoders();

		this.r = new SpeedControllerGroup(r0, r1);
		this.l = new SpeedControllerGroup(l0, l1);

		this.r.setInverted(false);
		this.l.setInverted(false);

		this.drive = new DifferentialDrive(l, r);

		this.drive.setSafetyEnabled(false);
		// new Thread(() -> {
		// double max = 0;
		// while (true) {
		// double speed = (l0.getSelectedSensorVelocity(0) +
		// r0.getSelectedSensorVelocity(0)) / 2;
		// if (speed > max) {
		// max = speed;
		// System.out.println(max);
		// }
		// }
		// }).run();

	}

	private void setUpEncoders() {
		r0.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		r0.setSensorPhase(false);
		r0.setSelectedSensorPosition(0, 0, 0);
		l0.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		l0.setSensorPhase(true);
		l0.setSelectedSensorPosition(0, 0, 0);
	}

	// Drive Methods
	public void arcade(double moveValue, double rotateValue) {
		this.drive.arcadeDrive(moveValue, rotateValue);
	}

	public void tank(double leftMoveValue, double rightMoveValue) {
		this.drive.tankDrive(leftMoveValue, rightMoveValue);
	}

	// ACCESSORS
	public double gyroRate() {
		return this.g.getRate();
	}

	public void gyroCalibrate() {
		this.g.calibrate();
	}

	public double getLeftVelocity() {
		return this.l0.getSelectedSensorVelocity(0);
	}

	public double getRightVelocity() {
		return this.r0.getSelectedSensorVelocity(0);
	}

	public double getLeftPosition() {
		return this.l0.getSelectedSensorPosition(0);
	}

	public double getRightPosition() {
		return this.r0.getSelectedSensorPosition(0);
	}

}
