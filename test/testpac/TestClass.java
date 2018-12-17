package testpac;

import com.kepler.db.KeplerException;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

//import generated.Maindocs;

public class TestClass {

    public static void main(String[] args) throws KeplerException, SQLException, NoSuchMethodException {
//        DriverManager.registerDriver(new org.postgresql.Driver());
//
//        KeplerManager db = new KeplerManager(KeplerManager.DefaultAction.CREATE, TestClass::getConnection, TestClass::returnConnection) ;
//        Student st = new Student();
//        java.util.Date birthDate = new java.util.Date(1990 - 1900, 10, 2);
//        st.birthDate = new Date(birthDate.getTime());
//        st.firstName = "Ivan";
//        st.middleName = "Мамражаев";
//
//        db.insert(st);


        java.io.File file = new java.io.File("E:/test.txt").getParentFile();
        Method method = java.io.File.class.getDeclaredMethod("sdfd", String.class);
        method.

        StringBuilder sb = new StringBuilder();
        try {
            java.io.FileReader reader = new java.io.FileReader(file);
            char[] buf = new char[100];
            while (reader.read(buf, 0, 100) > 0) {
                sb.append(buf);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        String[] s = file.list();
//        String msg = "";
//        if(!file.exists()){
//            msg += "created";
//            file.mkdirs();
//        }else {
//            msg += "exists";
//        }
        System.out.println(Arrays.toString(s));

//        [etc[..], selinux[], proc[..], mnt, lost+found, lib[..], vmlinuz, opt,
//                root, usr [lib, local, bin, sbin, include, games[], share, src], sys, lib64, home[], bin, dev, sbin, tmp,
//                srv, run[..], var, media, initrd.img, boot[..]]

//      boot:   [vmlinuz-3.8.0-29-generic, grub, memtest86+_multiboot.bin, System.map-3.8.0-29-generic, memtest86+.bin, abi-3.8.0-29-generic, initrd.img-3.8.0-29-generic, config-3.8.0-29-generic]

//        java.lang.NullPointerException: [.selected_editor, .local, slow_http_test,
//                libevent-2.0.so.5, .config [htop[htoprc], mc], .profile, slow_http_test.tar.gz, .w3m,
//        .bash_history, .bashrc, .cache, backup, .ssh, .mysql_history]

//      run:  [proftpd.pid, proftpd.scoreboard.lck, proftpd.scoreboard, nginx.pid, motd, init.upgraded, reboot-required.pkgs, reboot-required, tomcat7.pid, pppconfig, proftpd.delay, proftpd.sock, proftpd, atd.pid, crond.reboot, crond.pid, acpid.pid, acpid.socket, upstart-socket-bridge.pid, sshd.pid, munin, network, network-interface-security, rsyslogd.pid, sshd, screen, dbus, upstart-udev-bridge.pid, shm, lock, mount, sendsigs.omit.d, resolvconf, utmp, udev, initramfs]

//    proc:    [dri, sysrq-trigger, partitions, diskstats, crypto, key-users, version_signature,
// kpageflags, kpagecount, kmsg, kcore, softirqs, version, uptime, stat, meminfo,
// loadavg, interrupts, devices, cpuinfo, consoles, cmdline, locks, filesystems,
// swaps, vmallocinfo, slabinfo, zoneinfo, vmstat, pagetypeinfo, buddyinfo, latency_stats, kallsyms, modules,
// dma, timer_stats, timer_list, sched_debug, schedstat, iomem, ioports, execdomains, mdstat, scsi, misc, acpi,
// fb, mtrr, irq, cgroups, sys, bus, tty, driver, fs, sysvipc, net, mounts, self, 1, 2, 3, 5, 6, 7, 8, 9, 10,
// 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 25, 26, 27, 28, 29, 30, 31, 42, 43, 44, 45, 47, 67, 68, 194,
// 195, 302, 304, 367, 374, 434, 448, 449, 451, 621, 692, 769, 828, 837, 844, 845, 849, 862, 865, 866, 878, 912,
// 1274, 1345, 1735, 3942, 6635, 6827, 13368, 13369, 13376, 13377, 13378, 13381, 13382, 13383, 17316, 17317, 17318,
// 17319, 17320, 20003, 25400]

//      lib:  [libipq.so.0.0.0, libipq_pic.so.0.0.0, libfuse.so.2.8.6, libulockmgr.so.1.0.1, libiptc.so.0.0.0, libulockmgr.so.1, libnewt.so.0.52.11, libip6tc.so.0.0.0, libfuse.so.2, libxtables.so.7, libply.so.2.0.0, plymouth, libnl-3.so.200, lsb, init, libply-splash-core.so.2, xtables, apparmor, libply-splash-core.so.2.0.0, hdparm, libnl-genl-3.so.200.3.0, firmware, libply-boot-client.so.2, libnl-3.so.200.3.0, libnl-genl-3.so.200, libply-boot-client.so.2.0.0, libply.so.2, resolvconf, terminfo, libnewt.so.0.52, libply-splash-graphics.so.2, libipq_pic.so.0, libxtables.so.7.0.0, recovery-mode, libiw.so.30, systemd, x86_64-linux-gnu, udev, libipq.so.0, libip4tc.so.0, libip4tc.so.0.0.0, libproc-3.2.8.so, libip6tc.so.0, libply-splash-graphics.so.2.0.0, ufw, klibc-bhN-zLH5wUTKSCGch2ba2xqTtLE.so, modules, libdevmapper.so.1.02.1, libiptc.so.0]

        //for(long i = 1; i<= b; i++){
//for(int j = 0; j < Integer.MAX_VALUE; j++){
//    System.out.println(j);
//for(int k = 0; k < Integer.MAX_VALUE; k++){
//    System.out.println("just kidding");
//for(int z = 0; z < Integer.MAX_VALUE; z++){
//    System.out.println("nothing personal");
//}
//}
//}
//    result *= a;
//}
//     /etc:   [newt, cron.weekly, gshadow, lsb-release, rc.digitalocean, pam.d, nanorc,
//                vtrgb, nsswitch.conf, siege, modprobe.d, mailcap.order, bash_completion.d,
//                hosts.allow, at.deny, dhcp, insserv, popularity-contest.conf, landscape,
//                services, lsb-base-logging.sh, hdparm.conf, pam.conf, wpa_supplicant, profile, ifplugd, apport, apt, hosts,
//                w3m, gshadow-, bash.bashrc, ld.so.conf, passwd-, issue, cron.hourly, security, console-setup, crontab, updatedb.conf,
//                manpath.config, debian_version, tomcat7, networks, shells, cron.monthly, sudoers, insserv.conf, pm, init, .pwd.lock,
//                bindresvport.blacklist, ca-certificates.conf.dpkg-old, apparmor, libnl-3, rc2.d, ucf.conf, skel, profile.d, ppp,
//                screenrc, zsh_command_not_found, rc4.d, groff, apparmor.d, passwd, ld.so.cache, X11, logrotate.d, mke2fs.conf,
//                ltrace.conf, initramfs-tools, kbd, opt, host.conf, login.defs, adduser.conf, sensors3.conf, cron.daily, iscsi,
//                protocols, sensors.d, fonts, smi.conf, insserv.conf.d, kernel-img.conf, wireshark, rsyslog.conf, rpc, chatscripts,
//                rmt, ldap, munin, resolvconf, terminfo, ssh, rc5.d, magic.mime, apm, bash_completion, inputrc, proftpd, lsb-base,
//                ssl, update-notifier, debconf.conf, rc1.d, logrotate.conf, dbus-1, sysstat, depmod.d, rc3.d, mime.types, hosts.deny,
//                ca-certificates.conf, nginx, calendar, locale.alias, update-manager, mc, ghostscript, issue.net, deluser.conf, dpkg,
//                alternatives, authbind, rc6.d, hostname, localtime, resolv.conf, python, gai.conf, rc0.d, iproute2, systemd, ftpusers,
//                legal, ld.so.conf.d, sudoers.d, default, digitalocean, shadow, kernel, fstab, fstab.d, sysctl.d, xml, acpi, shadow-,
//                rsyslog.d, sgml, blkid.tab, mailcap, group-, udev, init.d, cron.d, ca-certificates, group, rcS.d, logcheck, byobu,
//                timezone, grub.d, securetty, perl, network, update-motd.d, ufw, rc.local, environment, trafshow, motd, mtab, fuse.conf,
//                python2.7, sysctl.conf, vim, os-release, modules, wgetrc, magic, blkid.conf]

    }



    private static Connection getConnection(){
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/Test", "postgres", "smartdoc");
        } catch (SQLException e) {
            e.getNextException();
            return null;
        }
    }

    private static void returnConnection(Connection conn){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void  test() {
//        ArrayList<String> strings = getCombinations(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
//        System.out.println(strings.size());
        String[] op = new String[8];
        Arrays.fill(op, "_");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(op));
        ArrayList<ArrayList<String>> accumulator = new ArrayList<>();
        accumulator.add(list);

        withOldMethod(list, "+", 7, accumulator);
//        System.out.println(accumulator.size());
        accumulator.forEach(System.out::println);
    }

//    private ArrayList<String> getCombinations(List<Object> vals) {
//        Arrays.asList(new String[vals.size() - 1]).stream()
//                .flatMap(place -> Arrays.asList("+", "*", "").stream()
//                                .map(oper ->
//                                                Collections.singletonList(oper).stream()
//                                )
//                )
//
//    }

    private void withOldMethod(ArrayList<String> list, String delim, int idx, ArrayList<ArrayList<String>> accumulator) {

        for (int i = 0; i <= idx; i++) {
            ArrayList<String> inner = (ArrayList<String>) list.clone();
            inner.set(i, delim);
            accumulator.add(inner);
            if (i > 0)
                withOldMethod(inner, delim, i - 1, accumulator);

        }
    }
}
