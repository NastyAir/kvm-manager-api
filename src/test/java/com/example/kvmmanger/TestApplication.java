package com.example.kvmmanger;

import org.libvirt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestApplication {

    private static final Logger logger = LoggerFactory.getLogger(TestApplication.class);
    public Connect kvmConn;


    /**
     * 初始化连接
     */
    public void init() {
        try {
            long start = System.currentTimeMillis();
            kvmConn = new Connect("qemu+tcp://10.6.30.105/system");
            long end = System.currentTimeMillis();
            System.out.println("time="+(end-start));
        } catch (LibvirtException e) {
            e.printStackTrace();
        }
//        doDetectHost(kvmConn);
    }

    // 物理主机探测
    private static void doDetectHost(Connect conn) {

        try {
//          System.out.println("Host name: " + conn.getHostName());
            System.out.println("Type: " + conn.getType());
            System.out.println(conn.getVersion());
            NodeInfo nodeInfo = conn.nodeInfo();

            System.out.println("每一个socket的核的数量: " + nodeInfo.cores);
            System.out.println("活动的cpu数量: " + nodeInfo.cpus);
            System.out.println("内存的大小（以千字节为单位）: " + nodeInfo.memory);
            System.out.println("预期的CPU频率: " + nodeInfo.mhz);
            System.out.println("cpu型号: " + nodeInfo.model);
            System.out.println("the number of NUMA cell, 1 for uniform: " + nodeInfo.nodes);
            System.out.println("每个节点的cpu socket数量: " + nodeInfo.sockets);
            System.out.println("每个核心的线程数: " + nodeInfo.threads);
            System.out.println("支持的CPU总数: " + nodeInfo.maxCpus());

//          for (String networkName : conn.listNetworks()) {
//              System.out.println("网络名称: " + networkName);
//              Network network = conn.networkLookupByName(networkName);
//              System.out.println("网络UUID: " + network.getUUIDString());
//              System.out.println("Network free: " + network.free());
//          }
//
//          for (String networkFilterName : conn.listNetworkFilters()) {
//              System.out.println("Network filter name: " + networkFilterName);
//          }
//          System.out.println(conn.getCapabilities());

        } catch (LibvirtException e) {
            e.printStackTrace();
        }
    }

    private void listStoragePool() throws LibvirtException {
        logger.info("list storage pool execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.105:16509/system", true);

        String[] pools = kvmConn.listStoragePools();
        logger.info("存储池个数：{}", pools.length);
        for (String pool : pools) {
            logger.info("存储池名称：{}", pool);
        }
    }

    private void getStoragePoolbyName(String name) throws LibvirtException {
        logger.info("get storage pool by name execute succeeded");
        logger.info("request parameter:{}", name);
//        Connect connect = new Connect("qemu+tcp://192.168.10.105:16509/system", true);
        StoragePool storagePool = kvmConn.storagePoolLookupByName(name);
        StoragePoolInfo storagePoolInfo = storagePool.getInfo();

        logger.info("存储池的状态：{}", storagePoolInfo.state);
        logger.info("存储池的容量：{}GB", storagePoolInfo.capacity / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的可用容量：{}GB", storagePoolInfo.available / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的已用容量：{}GB", storagePoolInfo.allocation / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的描述xml：\n {}", storagePool.getXMLDesc(0));
    }

    private void defineStoragePool() throws LibvirtException {
//        logger.info("define storage pool execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.231:16509/system");
//
//        // xml 文件 => Dom4j 文档 => String
//        SAXReader reader = new SAXReader();
//        Document document = reader.read(new File("xml/kvmdemo-storage-pool.xml"));
//        String xmlDesc = document.asXML();
        String xmlDesc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<pool type=\"dir\">\n" +
                "    <name>virtimage01</name> <!--名称必须唯一-->\n" +
                "    <source>\n" +
                "    </source>\n" +
                "    <capacity unit='GiB'>20</capacity> <!--StoragePool 的容量-->\n" +
                "    <allocation>0</allocation> <!--StoragePool 的已用容量-->\n" +
                "    <target>\n" +
                "        <path>/home/kvm/image</path> <!--StoragePool 在宿主机的路径-->\n" +
                "        <permissions> <!--权限-->\n" +
                "            <mode>0711</mode>\n" +
                "            <owner>0</owner>\n" +
                "            <group>0</group>\n" +
                "        </permissions>\n" +
                "    </target>\n" +
                "</pool>";
        logger.info("defineStoragePool description:\n{}", xmlDesc);

        StoragePool storagePool = kvmConn.storagePoolDefineXML(xmlDesc, 0);
        StoragePoolInfo storagePoolInfo = storagePool.getInfo();

        logger.info("存储池的状态：{}", storagePoolInfo.state);
        logger.info("存储池的容量：{}GB", storagePoolInfo.capacity / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的可用容量：{}GB", storagePoolInfo.available / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的已用容量：{}GB", storagePoolInfo.allocation / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的描述xml：\n {}", storagePool.getXMLDesc(0));
    }

    private void createStoragePool() throws LibvirtException {
        logger.info("create storage pool execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.105:16509/system");

        // xml 文件 => Dom4j 文档 => String
//        SAXReader reader = new SAXReader();
//        Document document = reader.read(new File("xml/kvmdemo-storage-pool.xml"));
//        String xmlDesc = document.asXML();
        String xmlDesc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<pool type=\"dir\">\n" +
                "    <name>virtimages</name> <!--名称必须唯一-->\n" +
                "    <source>\n" +
                "    </source>\n" +
                "    <capacity unit='GiB'>20</capacity> <!--StoragePool 的容量-->\n" +
                "    <allocation>0</allocation> <!--StoragePool 的已用容量-->\n" +
                "    <target>\n" +
                "        <path>/home/kvm/images</path> <!--StoragePool 在宿主机的路径-->\n" +
                "        <permissions> <!--权限-->\n" +
                "            <mode>0711</mode>\n" +
                "            <owner>0</owner>\n" +
                "            <group>0</group>\n" +
                "        </permissions>\n" +
                "    </target>\n" +
                "</pool>";

        StoragePool storagePool = kvmConn.storagePoolCreateXML(xmlDesc, 0);
        StoragePoolInfo storagePoolInfo = storagePool.getInfo();

        logger.info("存储池的状态：{}", storagePoolInfo.state);
        logger.info("存储池的容量：{}GB", storagePoolInfo.capacity / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的可用容量：{}GB", storagePoolInfo.available / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的已用容量：{}GB", storagePoolInfo.allocation / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的描述xml：\n {}", storagePool.getXMLDesc(0));
    }

    /*
        private void remoteConnectByTcp() throws LibvirtException {
            logger.info("remote connect execute succeeded");
            Connect connect = new Connect("qemu+tcp://192.168.71.147:16509/system", true);
            logger.info("连接到的宿主机的主机名：{}", connect.getHostName());
    //        logger.info("JNI连接的libvirt库版本号：{}", connect.getLibVirVersion());
            logger.info("连接的URI：{}", connect.getURI());
            logger.info("连接到的宿主机的剩余内存：{}", connect.getFreeMemory());
            logger.info("连接到的宿主机的最大Cpu数：{}", connect.getMaxVcpus(null));
            logger.info("hypervisor的名称：{}", connect.getType());
            logger.info("hypervisor的容量（返回的是一个xml字符串，用处不大）：\n{}", connect.getCapabilities());
            connect.close();
        }
    */
    private void deleteStoragePool(String name) throws LibvirtException {
        logger.info("delete storage pool execute succeeded");
//    Connect connect = new Connect("qemu+tcp://192.168.10.231:16509/system");

        StoragePool storagePool = kvmConn.storagePoolLookupByName(name);
        logger.info("存储池名称：{}", storagePool.getName());
//        storagePool.free();
        storagePool.destroy();
        storagePool.undefine();
    }

    private void listStorageVolume() throws LibvirtException {
        logger.info("list storage volume execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.231:16509/system", true);

        StoragePool storagePool = kvmConn.storagePoolLookupByName("virtimage01");
        String[] volumes = storagePool.listVolumes();
        logger.info("存储卷个数：{}", volumes.length);
        for (String volume : volumes) {
            if (volume.contains("iso")) {
                continue;
            } // 过滤掉 iso 文件
            logger.info("存储卷名称：{}", volume);
        }
    }

    private void getStorageVolumebyName(String storagePoolName,String storageVolName) throws LibvirtException {
        logger.info("get storage volume by name execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.231:16509/system", true);

        StoragePool storagePool = kvmConn.storagePoolLookupByName(storagePoolName);
        String[] volumes = storagePool.listVolumes();
//        for (String volume : volumes) {
//            if (volume.contains("iso")) continue; // 过滤掉 iso 文件

            StorageVol storageVol = storagePool.storageVolLookupByName(storageVolName);
            StorageVolInfo storageVolInfo = storageVol.getInfo();

            logger.info("存储卷名称：{}", storageVol.getName());
            logger.info("存储卷的类型：{}", storageVolInfo.type);
            logger.info("存储卷的容量：{} GB", storageVolInfo.capacity / 1024.00 / 1024.00 / 1024.00);
            logger.info("存储卷的可用容量：{} GB", (storageVolInfo.capacity - storageVolInfo.allocation) / 1024.00 / 1024.00 / 1024.00);
            logger.info("存储卷的已用容量：{} GB", storageVolInfo.allocation / 1024.00 / 1024.00 / 1024.00);
            logger.info("存储卷的描述xml：\n {}", storageVol.getXMLDesc(0));
//        }
    }

    private void createStorageVolume(String name) throws LibvirtException {
        logger.info("create storage volume execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.231:16509/system");

        // xml 文件 => Dom4j 文档 => String
//        SAXReader reader = new SAXReader();
//        Document document = reader.read(new File("xml/kvmdemo-storage-vol.xml"));
//        String xmlDesc = document.asXML();
        String xmlDesc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<volume type='file'>\n" +
                "    <name>kvmdemo.qcow2</name> <!--名称必须唯一-->\n" +
                "    <source>\n" +
                "    </source>\n" +
                "    <capacity unit='GiB'>10</capacity> <!--StorageVol 的容量-->\n" +
                "    <allocation>0</allocation> <!--StorageVol 的已用容量-->\n" +
                "    <target>\n" +
                "        <path>/var/lib/libvirt/images/kvmdemo.qcow2</path> <!--StorageVol 在宿主机的路径-->\n" +
                "        <format type='qcow2'/> <!--文件类型，通常都是 qcow2，也可以是 raw-->\n" +
                "        <permissions> <!--权限-->\n" +
                "            <mode>0600</mode>\n" +
                "            <owner>0</owner>\n" +
                "            <group>0</group>\n" +
                "        </permissions>\n" +
                "    </target>\n" +
                "</volume>";
        logger.info("createStorageVolume description:\n{}", xmlDesc);

        StoragePool storagePool = kvmConn.storagePoolLookupByName(name);
        logger.info("This could take some times at least 3min...");
        StorageVol storageVol = storagePool.storageVolCreateXML(xmlDesc, 0);
        logger.info("create success");
        logger.info("createStorageVolume name:{}", storageVol.getName());
        logger.info("createStorageVolume path:{}", storageVol.getPath());
        StorageVolInfo storageVolInfo = storageVol.getInfo();

        logger.info("存储卷的类型：{}", storageVolInfo.type);
        logger.info("存储卷的容量：{} GB", storageVolInfo.capacity / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储卷的可用容量：{} GB", (storageVolInfo.capacity - storageVolInfo.allocation) / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储卷的已用容量：{} GB", storageVolInfo.allocation / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储卷的描述xml：\n {}", storageVol.getXMLDesc(0));
    }

    //todo test
    private void cloneStorageVolume(String storagePoolName ,String sourceStorageVolName) throws LibvirtException {
        logger.info("clone storage volume execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.231:16509/system");

        // xml 文件 => Dom4j 文档 => String
//        SAXReader reader = new SAXReader();
//        Document document = reader.read(new File("xml/kvmdemo-storage-vol.xml"));
//        String xmlDesc = document.asXML();
        String xmlDesc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<volume type='file'>\n" +
                "    <name>kvmdemo2.qcow2</name> <!--名称必须唯一-->\n" +
                "    <source>\n" +
                "    </source>\n" +
                "    <capacity unit='GiB'>10</capacity> <!--StorageVol 的容量-->\n" +
                "    <allocation>0</allocation> <!--StorageVol 的已用容量-->\n" +
                "    <target>\n" +
                "        <path>/var/lib/libvirt/images/kvmdemo2.qcow2</path> <!--StorageVol 在宿主机的路径-->\n" +
                "        <format type='qcow2'/> <!--文件类型，通常都是 qcow2，也可以是 raw-->\n" +
                "        <permissions> <!--权限-->\n" +
                "            <mode>0600</mode>\n" +
                "            <owner>0</owner>\n" +
                "            <group>0</group>\n" +
                "        </permissions>\n" +
                "    </target>\n" +
                "</volume>";
        logger.info("createStorageVolume description:\n{}", xmlDesc);

        StoragePool storagePool = kvmConn.storagePoolLookupByName(storagePoolName);
        // 克隆的基镜像，这个镜像需要自己制作，可使用 virt-manager 制作基镜像，本示例代码采用的基镜像是 Ubuntu 16.04 64位
        StorageVol genericVol = storagePool.storageVolLookupByName(sourceStorageVolName);
        logger.info("This could take some times at least 3min...");
        StorageVol storageVol = storagePool.storageVolCreateXMLFrom(xmlDesc, genericVol, 0);
        logger.info("clone success");
        logger.info("createStorageVolume name:{}", storageVol.getName());
        logger.info("createStorageVolume path:{}", storageVol.getPath());
        StorageVolInfo storageVolInfo = storageVol.getInfo();

        logger.info("存储卷的类型：{}", storageVolInfo.type);
        logger.info("存储卷的容量：{} GB", storageVolInfo.capacity / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储卷的可用容量：{} GB", (storageVolInfo.capacity - storageVolInfo.allocation) / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储卷的已用容量：{} GB", storageVolInfo.allocation / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储卷的描述xml：\n {}", storageVol.getXMLDesc(0));
    }
    private void deleteStorageVolume(String storagePoolName,String storageVolName) throws LibvirtException {
        logger.info("delete storage volume execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.231:16509/system");

        StoragePool storagePool = kvmConn.storagePoolLookupByName(storagePoolName);
        StorageVol storageVol = storagePool.storageVolLookupByName(storageVolName);
        logger.info("存储卷名称：{}", storageVol.getName());
        storageVol.wipe();
        storageVol.delete(0);
    }

    private void listDomain() throws LibvirtException {
        logger.info("list domain execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.231:16509/system", true);

        int[] idsOfDomain = kvmConn.listDomains();
        logger.info("正在运行的虚拟机个数：{}", idsOfDomain.length);
        for (int id : idsOfDomain) {
            logger.info("正在运行的虚拟机id：{}", id);
        }

        String[] namesOfDefinedDomain = kvmConn.listDefinedDomains();
        logger.info("已定义未运行的虚拟机个数：{}", namesOfDefinedDomain.length);
        for (String name : namesOfDefinedDomain) {
            logger.info("已定义未运行的虚拟机名称：{}", name);
        }
    }

    private void createDomain() throws LibvirtException {
        logger.info("create domain execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.231:16509/system");

        // xml 文件 => Dom4j 文档 => String
//        SAXReader reader = new SAXReader();
//        Document document = reader.read(new File("xml/kvmdemo.xml"));
//        String xmlDesc = document.asXML();
        String xmlDesc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<domain type='kvm'>\n" +
                "    <name>kvmdemo1</name> <!--名称必须唯一-->\n" +
                "    <uuid>c6e408f3-7750-47ca-8bd1-d19837271411</uuid> <!--uuid必须唯一，可使用 java.util.UUID 随机生成-->\n" +
                "    <memory unit='MiB'>512</memory> <!--最大可用内存配置-->\n" +
                "    <currentMemory unit='MiB'>512</currentMemory>\n" +
                "    <vcpu placement='static'>1</vcpu> <!--配置cpu-->\n" +
                "    <os>\n" +
                "        <type arch='x86_64' machine='pc'>hvm</type>\n" +
                "        <boot dev='hd'/> <!--硬盘启动-->\n" +
                "        <boot dev='cdrom'/> <!--光驱启动-->\n" +
                "    </os>\n" +
                "    <features>\n" +
                "        <acpi/>\n" +
                "        <apic/>\n" +
                "        <pae/>\n" +
                "    </features>\n" +
                "    <clock offset='localtime'/>\n" +
                "    <on_poweroff>destroy</on_poweroff>\n" +
                "    <on_reboot>restart</on_reboot>\n" +
                "    <on_crash>restart</on_crash>\n" +
                "    <devices>\n" +
                "        <emulator>/usr/libexec/qemu-kvm</emulator> <!--模拟器所在路径，视自己情况配置-->\n" +
                "        <disk type='file' device='disk'>\n" +
                "            <driver name='qemu' type='qcow2'/>\n" +
                "            <source file='/home/kvm/image/kvmdemo.qcow2'/> <!--虚拟硬盘配置，这个地方填生成的镜像文件所在的路径即可-->\n" +
                "            <target dev='hda' bus='ide'/>\n" +
                "        </disk>\n" +
                "        <disk type='file' device='cdrom'>\n" +
                "            <source file='/home/kvm/TinyCore-current.iso'/>\n" +
                "            <target dev='hdb' bus='ide'/>\n" +
                "            <readonly/>\n" +
                "        </disk>\n" +
                "        <interface type='bridge'> <!--网络配置，本示例配置成桥接模式-->\n" +
                "            <mac address='52:54:00:f4:06:03'/> <!--mac 地址必须唯一-->\n" +
                "            <source bridge='br0'/>\n" +
                "        </interface>\n" +
                "        <console type='pty'> <!--控制台配置，如果需要使用 virsh 命令登陆虚拟机，则必须添加-->\n" +
                "            <target port='0'/>\n" +
                "        </console>\n" +
                "        <input type='tablet' bus='usb'/>\n" +
                "        <input type='mouse' bus='ps2'/>\n" +
                "        <input type='keyboard' bus='ps2'/>\n" +
                "        <graphics type='vnc' autoport='yes' keymap='en-us'\n" +
                "                  listen='0.0.0.0'/> <!--VNC配置，autoport=\"yes\"表示自动分配VNC端口，推荐使用，listen=\"0.0.0.0\"表示监听所有IP-->\n" +
                "        <memballoon model=\"virtio\"> <!--内存监控配置，添加此配置，才能正常取得内存使用情况-->\n" +
                "            <stats period=\"10\"/><!--每10s钟收集一次-->\n" +
                "        </memballoon>\n" +
                "    </devices>\n" +
                "</domain>";
        logger.info("createDomain description:\n{}", xmlDesc);

        Domain domain = kvmConn.domainCreateXML(xmlDesc, 0);

        logger.info("虚拟机的id：{}", domain.getID());
        logger.info("虚拟机的uuid：{}", domain.getUUIDString());
        logger.info("虚拟机的名称：{}", domain.getName());
        logger.info("虚拟机的是否自动启动：{}", domain.getAutostart());
        logger.info("虚拟机的状态：{}", domain.getInfo().state);
        logger.info("虚拟机的描述xml：\n{}", domain.getXMLDesc(0));
    }

    private void defineDomain() throws LibvirtException {
        logger.info("define domain execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.231:16509/system");

        // xml 文件 => Dom4j 文档 => String
//        SAXReader reader = new SAXReader();
//        Document document = reader.read(new File("xml/kvmdemo.xml"));
        String xmlDesc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<domain type='kvm'>\n" +
                "    <name>kvmdemo</name> <!--名称必须唯一-->\n" +
                "    <uuid>c6e408f3-7750-47ca-8bd1-d19837271472</uuid> <!--uuid必须唯一，可使用 java.util.UUID 随机生成-->\n" +
                "    <memory unit='MiB'>512</memory> <!--最大可用内存配置-->\n" +
                "    <currentMemory unit='MiB'>512</currentMemory>\n" +
                "    <vcpu placement='static'>1</vcpu> <!--配置cpu-->\n" +
                "    <os>\n" +
                "        <type arch='x86_64' machine='pc'>hvm</type>\n" +
                "        <boot dev='hd'/> <!--硬盘启动-->\n" +
                "        <boot dev='cdrom'/> <!--光驱启动-->\n" +
                "    </os>\n" +
                "    <features>\n" +
                "        <acpi/>\n" +
                "        <apic/>\n" +
                "        <pae/>\n" +
                "    </features>\n" +
                "    <clock offset='localtime'/>\n" +
                "    <on_poweroff>destroy</on_poweroff>\n" +
                "    <on_reboot>restart</on_reboot>\n" +
                "    <on_crash>restart</on_crash>\n" +
                "    <devices>\n" +
                "        <emulator>/usr/libexec/qemu-kvm</emulator> <!--模拟器所在路径，视自己情况配置-->\n" +
                "        <disk type='file' device='disk'>\n" +
                "            <driver name='qemu' type='qcow2'/>\n" +
                "            <source file='/home/kvm/image/kvmdemo.qcow2'/> <!--虚拟硬盘配置，这个地方填生成的镜像文件所在的路径即可-->\n" +
                "            <target dev='hda' bus='ide'/>\n" +
                "        </disk>\n" +
                "        <!--<disk type='file' device='cdrom'>\n" +
                "            <source file='/home/kvm/TinyCore-current.iso'/>\n" +
                "            <target dev='hdb' bus='ide'/>\n" +
                "            <readonly/>\n" +
                "        </disk>-->\n" +
                "        <interface type='bridge'> <!--网络配置，本示例配置成桥接模式-->\n" +
                "            <mac address='52:54:00:f4:06:03'/> <!--mac 地址必须唯一-->\n" +
                "            <source bridge='br0'/>\n" +
                "        </interface>\n" +
                "        <console type='pty'> <!--控制台配置，如果需要使用 virsh 命令登陆虚拟机，则必须添加-->\n" +
                "            <target port='0'/>\n" +
                "        </console>\n" +
                "        <input type='tablet' bus='usb'/>\n" +
                "        <input type='mouse' bus='ps2'/>\n" +
                "        <input type='keyboard' bus='ps2'/>\n" +
                "        <graphics type='vnc' autoport='yes' keymap='en-us'\n" +
                "                  listen='0.0.0.0'/> <!--VNC配置，autoport=\"yes\"表示自动分配VNC端口，推荐使用，listen=\"0.0.0.0\"表示监听所有IP-->\n" +
                "        <memballoon model=\"virtio\"> <!--内存监控配置，添加此配置，才能正常取得内存使用情况-->\n" +
                "            <stats period=\"10\"/><!--每10s钟收集一次-->\n" +
                "        </memballoon>\n" +
                "    </devices>\n" +
                "</domain>";
        logger.info("defineDomain description:\n{}", xmlDesc);

        Domain domain = kvmConn.domainDefineXML(xmlDesc);
//        domain.abortJob();
        // 是否随宿主机开机自动启动
        domain.setAutostart(false);

        domain.create(); // 定义完后直接启动
        logger.info("虚拟机的id：{}", domain.getID());
        logger.info("虚拟机的uuid：{}", domain.getUUIDString());
        logger.info("虚拟机的名称：{}", domain.getName());
        logger.info("虚拟机的是否自动启动：{}", domain.getAutostart());
        logger.info("虚拟机的状态：{}", domain.getInfo().state);
        logger.info("虚拟机的描述xml：\n{}", domain.getXMLDesc(0));
    }
    private void undefineDomain(String domainName) throws LibvirtException {
        logger.info("undefine domain execute succeeded");
//        Connect connect = new Connect("qemu+tcp://192.168.10.231:16509/system");

        Domain domain = kvmConn.domainLookupByName(domainName);
        logger.info("虚拟机的id：{}", domain.getID());
        logger.info("虚拟机的uuid：{}", domain.getUUIDString());
        logger.info("虚拟机的名称：{}", domain.getName());
        logger.info("虚拟机的是否自动启动：{}", domain.getAutostart());
        logger.info("虚拟机的状态：{}", domain.getInfo().state);
        domain.destroy(); // 强制关机
        domain.undefine();
    }
    public static void main(String[] args) throws LibvirtException {

        TestApplication application = new TestApplication();
        application.init();
//        application.remoteConnectByTcp();
//        application.createStoragePool();
        application.listStoragePool();
//        application.destroyStoragePool();
        application.getStoragePoolbyName("virtimages");
//        application.getStoragePoolbyName("virtimage01");

//        application.activePool();
//        application.defineStoragePool();
//        application.listStoragePool();
//        application.createStorageVolume("virtimage01");
//        application.cloneStorageVolume("virtimage01","kvmdemo.qcow2");
//        application.listStorageVolume();
//        application.getStorageVolumebyName("virtimage01","kvmdemo.qcow2");
//        application.deleteStorageVolume("virtimage01","kvmdemo.qcow2");
//        application.listDomain();
//        application.getDomainbyId(3);
//        application.createDomain();

    }

    private void getDomainbyId(int id) throws LibvirtException {
        Domain domain = kvmConn.domainLookupByID(id);
        logger.info("虚拟机的id：{}", domain.getID());
        logger.info("虚拟机的uuid：{}", domain.getUUIDString());
        logger.info("虚拟机的名称：{}", domain.getName());
        logger.info("虚拟机的是否自动启动：{}", domain.getAutostart());
        logger.info("虚拟机的状态：{}", domain.getInfo().state);
    }

    private void activePool() throws LibvirtException {
        kvmConn.storagePoolLookupByName("virtimage01").create(0);
    }

    private void destroyStoragePool() throws LibvirtException {
        kvmConn.storagePoolLookupByName("virtimages").destroy();

    }
}
