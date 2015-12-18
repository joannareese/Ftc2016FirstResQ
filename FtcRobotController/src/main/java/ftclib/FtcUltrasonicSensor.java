package ftclib;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import hallib.HalUtil;
import trclib.TrcAnalogInput;
import trclib.TrcDbgTrace;
import trclib.TrcFilter;

/**
 * This class implements a platform dependent ultrasonic sensor
 * extending TrcAnalogInput. It provides implementation of the
 * abstract methods in TrcAnalogInput.
 */
public class FtcUltrasonicSensor extends TrcAnalogInput
{
    private static final String moduleName = "FtcUltrasonicSensor";
    private static final boolean debugEnabled = false;
    private TrcDbgTrace dbgTrace = null;

    private UltrasonicSensor sensor;

    /**
     * Constructor: Creates an instance of the object.
     *
     * @param hardwareMap specifies the global hardware map.
     * @param instanceName specifies the instance name.
     * @param filter specifies a filter object used for filtering sensor noise.
     *               If none needed, it can be set to null.
     */
    public FtcUltrasonicSensor(HardwareMap hardwareMap, String instanceName, TrcFilter filter)
    {
        super(instanceName, 0, filter);

        if (debugEnabled)
        {
            dbgTrace = new TrcDbgTrace(moduleName + "." + instanceName,
                                       false,
                                       TrcDbgTrace.TraceLevel.API,
                                       TrcDbgTrace.MsgLevel.INFO);
        }

        sensor = hardwareMap.ultrasonicSensor.get(instanceName);
    }   //FtcUltrasonicSensor

    /**
     * Constructor: Creates an instance of the object.
     *
     * @param instanceName specifies the instance name.
     * @param filter specifies a filter object used for filtering sensor noise.
     *               If none needed, it can be set to null.
     */
    public FtcUltrasonicSensor(String instanceName, TrcFilter filter)
    {
        this(FtcOpMode.getInstance().hardwareMap, instanceName, filter);
    }   //FtcUltrasonicSensor

    /**
     * Constructor: Creates an instance of the object.
     *
     * @param instanceName specifies the instance name.
     */
    public FtcUltrasonicSensor(String instanceName)
    {
        this(instanceName, null);
    }   //FtcUltrasonicSensor

    /**
     * This method calibrates the sensor.
     */
    public void calibrate()
    {
        calibrate(DataType.INPUT_DATA);
    }   //calibrate

    //
    // Implements TrcAnalogInput abstract methods.
    //

    /**
     * This method returns the raw sensor data of the specified type.
     *
     * @return raw sensor data of the specified type.
     */
    @Override
    public SensorData getRawData(DataType dataType)
    {
        final String funcName = "getRawData";
        SensorData data = null;

        //
        // Ultrasonic sensor supports only INPUT_DATA type.
        //
        if (dataType == DataType.INPUT_DATA)
        {
            data = new SensorData(HalUtil.getCurrentTime(), sensor.getUltrasonicLevel());
        }
        else
        {
            throw new UnsupportedOperationException(
                    "Ultrasonic sensor only support INPUT_DATA type.");
        }

        if (debugEnabled)
        {
            dbgTrace.traceEnter(funcName, TrcDbgTrace.TraceLevel.API);
            dbgTrace.traceExit(funcName, TrcDbgTrace.TraceLevel.API,
                               "=(timestamp:%.3f,value=%f)", data.timestamp, data.value);
        }

        return data;
    }   //getRawData

}   //class FtcUltrasonicSensor
