#!/bin/bash


PRJ_DIR=$PWD

mount /dev/sdb1 /mnt
cp $PRJ_DIR/deployed/* /mnt/Software
sync
umount /mnt

