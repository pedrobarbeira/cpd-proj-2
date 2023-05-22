package org.cpd.client.controller;

public record ServerMessage(String message,boolean doRespond,boolean isOver) {
}
