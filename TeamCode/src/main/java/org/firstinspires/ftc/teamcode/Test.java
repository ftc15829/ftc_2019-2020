package org.firstinspires.ftc.teamcode;
import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import static java.lang.Math.signum;

//@Disabled
@TeleOp(name="Test") public class Test extends LinearOpMode {

	// Initializes hardware
	private DcMotor leftFront,  rightBack, rightFront, leftBack;
	private DcMotor scissor, scissor2, slideL, slideR;
	private Servo hook, block1, block2;

	private double ls_x, ls_y, rs_x, rs_y, lt, rt;
    private double ls_x_, ls_y_, rs_x_, rs_y_, lt_, rt_;
    private boolean b_a, b_b;
    private boolean b_a_, b_b_;

    // Runs when initialized
    @SuppressLint("all") @Override public void runOpMode() {

        // Assigns main drive motors to corresponding hardware map id's
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        // Main drive motor setup
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);

        // Assigns scissor stuff
        scissor = hardwareMap.dcMotor.get("scissor");
        scissor2 = hardwareMap.dcMotor.get("scissor2");
        slideL = hardwareMap.dcMotor.get("slideL");
        slideR = hardwareMap.dcMotor.get("slideR");
        // Assigns hook stuff
        hook = hardwareMap.servo.get("hook");
        block1 = hardwareMap.servo.get("block1");
        block2 = hardwareMap.servo.get("block2");

        // Update loop
        waitForStart();
        while (opModeIsActive()) {

            // Updates gamepad
            updateGamePad();

            // Updates hardware
            updateMainDrive();
            updateServos();
            updateAuxMotors();

            // Updates telemetry
            telemetry.addData("Gamepad 1", String.format(
                    "rs_x: %f rs_y: %f\nls_x: %f ls_y: %f\nlt: %f rt: %f\n b_a: %b b_b: %b",
                    rs_x, rs_y, ls_x, ls_y, lt, rt, b_a, b_b));
            telemetry.addData("Gamepad 2", String.format(
                    "rs_x_: %f rs_y_: %f\nls_x_: %f ls_y_: %f\nlt_: %f rt_: %f\n b_a_: %b b_b_: %b",
                    rs_x_, rs_y_, ls_x_, ls_y_, lt_, rt_, b_a_, b_b_));
            telemetry.addData("Main Drive", String.format(
                    "lf: |%5f| rf: |%5f|\nlb: |%5f| rb: |%5f|",
                    getPower(0), getPower(3), getPower(2), getPower(1)));
            telemetry.addData("Servos", "");
            telemetry.update();
        }
    }

    // UPDATE gamepad
	private void updateGamePad() {
		rs_x = gamepad1.right_stick_x;
		rs_y = gamepad1.right_stick_y;
		ls_x = gamepad1.left_stick_x;
		b_b = gamepad1.b;
		rs_y_ = gamepad2.right_stick_y;
		ls_y_ = gamepad2.left_stick_y;
		lt_ = gamepad2.left_trigger;
		rt_ = gamepad2.right_trigger;
	}

	// UPDATE aux motors
	private void updateAuxMotors() {
		scissor.setPower(ls_y_ != 0.0 ? signum(ls_y_) * -0.2 : 0.0);
		scissor2.setPower(ls_y_ != 0.0 ? signum(ls_y_) * -1.0 : 0.0);
		
		slideL.setPower(rs_y_ != 0.0 ? signum(rs_y_) * -0.5 : 0.0);
		slideR.setPower(rs_y_ != 0.0 ? signum(rs_y_) * 0.5 : 0.0);
	}

	// UPDATE servos
	private boolean a = true; // Toggles
	private void updateServos() { 
		// Pressing B toggles the hook between ???
		if (b_b) {
			hook.setPosition(a ? 0.5 : 0.0);
			a = !a;
			sleep(100);
		}
		// Sets block positions
		if (lt_ != 0.0 || rt_ != 0.0) {
			block1.setPosition(rt_ != 0.0 ? 0.0 : 1.0);
			block2.setPosition(rt_ != 0.0 ? 1.0 : 0.0);
		}
	}

    // UPDATE main drive motors
    private void updateMainDrive() {
        DcMotor[] driveMotors = new DcMotor[] {leftFront, rightBack, leftBack, rightFront};
        for (int i = 0; i < driveMotors.length; i++)
            driveMotors[i].setPower(getPower(i));
    }
    // Returns power for given motor:
	private double getPower(int i) {
		double power;
		// Chooses a value for power based on given motor (i)
		switch (i) {
			case 0: power = rs_y - ls_x - rs_x; break; // leftFront
			case 1: power = rs_y - ls_x + rs_x; break; // rightBack
			case 2: power = rs_y + ls_x - rs_x; break; // leftBack
			case 3: power = rs_y + ls_x + rs_x; break; // rightFront
			default: power = 0; break;
		}
		return power;
	}
}
