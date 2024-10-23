package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.util.setpose;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class mecanum_test extends LinearOpMode {
    boolean touchpadpressed = false;
    boolean slowmode = false;
    boolean touchpadwpressed = false;
    boolean clawclose = false;
    boolean clawopen = false;
    boolean hangup = false;
    boolean hangdown = false;
    boolean unloaded = false;
    boolean loaded = false;

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        Servo clawServo = hardwareMap.servo.get("clawServo");
        Servo primeServo = hardwareMap.servo.get("primeServo");
        DcMotor armMotor1 = hardwareMap.dcMotor.get("armMotor1");
        DcMotor hangmotor = hardwareMap.dcMotor.get("hangmotor");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        hangmotor.setDirection(DcMotorSimple.Direction.FORWARD);


        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;
            clawclose = gamepad1.right_bumper;
            clawopen = gamepad1.left_bumper;
            loaded = gamepad1.b;//Set these differently
            unloaded = gamepad1.a;//Set these differently
            hangup = gamepad1.dpad_up;
            hangdown = gamepad1.dpad_down;
            touchpadpressed = gamepad1.touchpad;
            if (touchpadpressed && ! touchpadwpressed) {
                slowmode = ! slowmode;
            }
            double slowmodemultiplier = 0.5;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double multiplier = 1; if (slowmode){multiplier = slowmodemultiplier;};
            double frontLeftPower = ((y - x + rx) / denominator) * multiplier;
            double backLeftPower = ((y + x + rx) / denominator) * multiplier;
            double frontRightPower = ((y - x - rx) / denominator) * multiplier;
            double backRightPower = ((y + x - rx) / denominator) * multiplier;

           double armpower = 0;
           if (gamepad1.right_trigger > 0) {
               armpower = gamepad1.right_trigger;
           } else if (gamepad1.left_trigger > 0) {
               armpower = -gamepad1.left_trigger;
           }
            armpower = Range.clip(armpower, -0.5,0.5);

           double hangpower = 0;
           if (gamepad1.dpad_up) {
               hangpower = 1;
           } else if (gamepad1.dpad_down) {
               hangpower = -1;
           }
//           double clawpower = 0.5;
           if (gamepad1.right_bumper) {
               setpose(clawServo, 110);   // closing degree (grip force)
           }   else if (gamepad1.left_bumper) {
               setpose(clawServo,  60);  // opening degree keep positive otherwise will hit edges
            }
           if (gamepad1.b) {
               setpose(primeServo, 110);//change degrees.
           } else if (gamepad1.a) {
               setpose(primeServo, 60);//change degrees
           }

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);
            armMotor1.setPower(armpower);
            hangmotor.setPower(hangpower);
            touchpadwpressed = touchpadpressed;
            telemetry.addData("slowmode",slowmode);
            telemetry.update();
        }
    }
}