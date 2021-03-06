package net.coderbot.patchwork.event;

import java.util.function.Consumer;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

public class SubscribeEventHandler extends AnnotationVisitor {
	Consumer<SubscribeEvent> consumer;
	SubscribeEvent instance;

	public SubscribeEventHandler(int access,
			String name,
			String descriptor,
			String signature,
			Consumer<SubscribeEvent> consumer) {
		super(Opcodes.ASM7);

		this.consumer = consumer;
		this.instance = new SubscribeEvent(access, name, descriptor, signature);
	}

	@Override
	public void visit(final String name, final Object value) {
		super.visit(name, value);

		// TODO: Not tested yet
		System.out.println(name + "->" + value);

		if(name.equals("receiveCancelled")) {
			instance.receiveCancelled = value == Boolean.TRUE;
		} else {
			System.err.println("Unexpected SubscribeEvent property: " + name + "->" + value);
		}
	}

	@Override
	public void visitEnum(final String name, final String descriptor, final String value) {
		super.visitEnum(name, descriptor, value);

		if(!name.equals("priority")) {
			System.err.println("Unexpected SubscribeEvent enum property: " + name + "->" +
							   descriptor + "::" + value);

			return;
		}

		// TODO! not tested yet, wrong name to test
		if(!descriptor.equals("Lnet/minecraftforge/fml/common/Mod$EventBusSubscriber$Bus;")) {
			System.out.println(
					"Unexpected descriptor for SubscribeEvent bus property, continuing anyways: " +
					descriptor);
		}

		instance.priority = value;
	}

	@Override
	public void visitEnd() {
		super.visitEnd();

		consumer.accept(instance);
	}
}
