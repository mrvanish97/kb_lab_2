cmake_minimum_required(VERSION 3.12)
project(cryptolib)

set(CMAKE_CXX_STANDARD 14)

add_library(cryptolib SHARED library.cpp library.h)