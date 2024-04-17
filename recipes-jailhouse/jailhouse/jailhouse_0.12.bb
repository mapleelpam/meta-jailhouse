
SUMMARY = "Recipe for the Jailhouse Hypervisor/ Altera Device Only"
HOMEPAGE = "https://github.com/siemens/jailhouse"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=9fa7f895f96bde2d47fd5b7d95b6ba4d"

SRC_URI = "git://github.com/siemens/jailhouse.git;protocol=https;branch=master"
SRC_URI += "file://0001-configs-add-socfpga-s10socdk-and-socfpga-s10socdk-in.patch"

PV = "0.12+git"
SRCREV = "e57d1eff6d55aeed5f977fe4e2acfb6ccbdd7560"

S = "${WORKDIR}/git"

inherit module bash-completion

RDEPENDS:${PN} += "python3-core"

DEPENDS = "virtual/kernel make-native python3-mako-native dtc-native python3-mako"

TOOLS_SRC_DIR = "${S}/tools"
TOOLS_OBJ_DIR = "${S}/tools"

do_compile() {
   oe_runmake V=1 \
        ARCH=arm64 CROSS_COMPILE=${TARGET_PREFIX} \
        KDIR=${STAGING_KERNEL_BUILDDIR}

}

do_install() {
    oe_runmake \
               ARCH=arm64 \
               CROSS_COMPILE=${TARGET_PREFIX} \
               KDIR=${STAGING_KERNEL_BUILDDIR} \
               DESTDIR=${D} install

    install -d ${D}${CELL_DIR}
    install ${B}/configs/*.cell ${D}${CELL_DIR}/

    install -d ${D}${INMATES_DIR}
    install ${B}/inmates/demos/${JH_ARCH}/*.bin ${D}${INMATES_DIR}
}


PACKAGE_BEFORE_PN = "kernel-module-jailhouse"
FILES_${PN} = "${base_libdir}/firmware ${libexecdir} ${sbindir} ${JH_DATADIR}"

KERNEL_MODULE_AUTOLOAD += "jailhouse"


