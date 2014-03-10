package net.neoremind.navi.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetUtils {
    private static final Logger LOG = LoggerFactory.getLogger(NetUtils.class);

    public static final int MAX_PORT = 65535;

    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static final Pattern ipPattern = Pattern.compile(IPADDRESS_PATTERN);

    public static final String LOCALHOST = "127.0.0.1";

    public static final String ANYHOST = "0.0.0.0";

    private static volatile InetAddress LOCAL_ADDRESS = null;

    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    private NetUtils() {

    }

    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
            LOG.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable e) {
                                    LOG.warn("Failed to retriving ip address, " + e.getMessage(), e);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        LOG.warn("Failed to retriving ip address, " + e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            LOG.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        LOG.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null && !ANYHOST.equals(name) && !LOCALHOST.equals(name) && IP_PATTERN.matcher(name).matches());
    }

    public static String getLocalHostIP() {
        InetAddress address = getLocalAddress();
        return address == null ? LOCALHOST : address.getHostAddress();
    }

    public static String filterLocalHost(String host) {
        if (NetUtils.isInvalidLocalHost(host)) {
            return NetUtils.getLocalHostIP();
        }
        return host;
    }

    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null)
            return LOCAL_ADDRESS;
        InetAddress localAddress = getLocalAddress0();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }

    public static boolean isInvalidLocalHost(String host) {
        return host == null || host.length() == 0 || host.equalsIgnoreCase("localhost") || host.equals("0.0.0.0")
                || (LOCAL_IP_PATTERN.matcher(host).matches());
    }

    public static boolean isLocalHost(String host) {
        return host != null && (LOCAL_IP_PATTERN.matcher(host).matches() || host.equalsIgnoreCase("localhost"));
    }

    public static String getIpByHost(String hostName) {
        try {
            return InetAddress.getByName(hostName).getHostAddress();
        } catch (UnknownHostException e) {
            return hostName;
        }
    }

    public static int getFreePort(String hostName, int startingPort, int endingPort) {
        int range = endingPort - startingPort;
        int port = 0;
        if (range > 0) {
            Random r = new Random();
            while (true) {
                port = r.nextInt(range + 1) + startingPort;
                if (isPortFree(hostName, port)) {
                    break;
                }
            }
        }
        return port;
    }

    public static boolean isPortFree(String hostName, int portNumber) {
        if (portNumber <= 0 || portNumber > MAX_PORT) {
            return false;
        }

        if (hostName == null || isThisMe(hostName)) {
            return isPortFreeServer(portNumber);
        } else {
            return isPortFreeClient(hostName, portNumber);
        }
    }

    public static boolean isPortFree(int portNumber) {
        return isPortFree(null, portNumber);
    }

    private static boolean isPortFreeClient(String hostName, int portNumber) {
        try {
            // If the host name is null, assume localhost
            if (hostName == null) {
                hostName = getHostName();
            }
            Socket socket = new Socket(hostName, portNumber);
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            os.close();
            os = null;
            is.close();
            is = null;
            socket.close();
            socket = null;
        } catch (Exception e) {
            // Nobody is listening on this port
            return true;
        }

        return false;
    }

    private static boolean isPortFreeServer(int port) {
        // check 3 different ip-port combinations.
        // Amazingly I have seen all 3 possibilities -- so just checking on
        // 0.0.0.0
        // is not good enough.
        // Usually it is the 0.0.0.0 -- but JMS (default:7676)
        // only returns false from the "localhost":port combination.
        // We want to be aggressively disqualifying ports rather than the other
        // way around

        try {
            byte[] allZero = new byte[] { 0, 0, 0, 0 };
            InetAddress add = InetAddress.getByAddress(allZero);

            if (isPortFreeServer(port, add) == false)
                return false; // return immediately on "not-free"

            add = InetAddress.getLocalHost();

            if (isPortFreeServer(port, add) == false)
                return false; // return immediately on "not-free"

            add = InetAddress.getByName("localhost");

            return isPortFreeServer(port, add);
        } catch (Exception e) {
            // If we can't get an IP address then we can't check
            return false;
        }

    }

    private static boolean isPortFreeServer(int port, InetAddress add) {
        try {
            ServerSocket ss = new ServerSocket(port, 100, add);
            ss.close();

            LOG.debug(add.toString() + " : " + port + " --> FREE");
            return true;
        } catch (Exception e) {
            LOG.debug(add.toString() + " : " + port + " --> IN USE");
            return false;
        }
    }

    private static boolean isThisMe(String hostname) {
        try {
            InetAddress[] myadds = getHostAddresses();
            InetAddress[] theiradds = InetAddress.getAllByName(hostname);

            for (int i = 0; i < theiradds.length; i++) {
                if (theiradds[i].isLoopbackAddress()) {
                    return true;
                }

                for (int j = 0; j < myadds.length; j++) {
                    if (myadds[j].equals(theiradds[i])) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }

        return false;
    }

    public static boolean isValidPort(int portNumber) {
        if (portNumber >= 0 && portNumber <= MAX_PORT) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidIP(String ip) {
        return ipPattern.matcher(ip).matches();
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return null;
        }
    }

    public static InetAddress[] getHostAddresses() {
        try {
            String hname = getHostName();

            if (hname == null) {
                return null;
            }

            return InetAddress.getAllByName(hname);
        } catch (Exception e) {
            return null;
        }
    }

}
