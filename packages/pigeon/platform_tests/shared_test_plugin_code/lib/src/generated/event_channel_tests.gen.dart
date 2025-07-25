// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
//
// Autogenerated from Pigeon, do not edit directly.
// See also: https://pub.dev/packages/pigeon
// ignore_for_file: public_member_api_docs, non_constant_identifier_names, avoid_as, unused_import, unnecessary_parenthesis, prefer_null_aware_operators, omit_local_variable_types, unused_shown_name, unnecessary_import, no_leading_underscores_for_local_identifiers

import 'dart:async';
import 'dart:typed_data' show Float64List, Int32List, Int64List, Uint8List;

import 'package:flutter/foundation.dart' show ReadBuffer, WriteBuffer;
import 'package:flutter/services.dart';

bool _deepEquals(Object? a, Object? b) {
  if (a is List && b is List) {
    return a.length == b.length &&
        a.indexed
            .every(((int, dynamic) item) => _deepEquals(item.$2, b[item.$1]));
  }
  if (a is Map && b is Map) {
    return a.length == b.length &&
        a.entries.every((MapEntry<Object?, Object?> entry) =>
            (b as Map<Object?, Object?>).containsKey(entry.key) &&
            _deepEquals(entry.value, b[entry.key]));
  }
  return a == b;
}

enum EventEnum {
  one,
  two,
  three,
  fortyTwo,
  fourHundredTwentyTwo,
}

enum AnotherEventEnum {
  justInCase,
}

/// A class containing all supported nullable types.
class EventAllNullableTypes {
  EventAllNullableTypes({
    this.aNullableBool,
    this.aNullableInt,
    this.aNullableInt64,
    this.aNullableDouble,
    this.aNullableByteArray,
    this.aNullable4ByteArray,
    this.aNullable8ByteArray,
    this.aNullableFloatArray,
    this.aNullableEnum,
    this.anotherNullableEnum,
    this.aNullableString,
    this.aNullableObject,
    this.allNullableTypes,
    this.list,
    this.stringList,
    this.intList,
    this.doubleList,
    this.boolList,
    this.enumList,
    this.objectList,
    this.listList,
    this.mapList,
    this.recursiveClassList,
    this.map,
    this.stringMap,
    this.intMap,
    this.enumMap,
    this.objectMap,
    this.listMap,
    this.mapMap,
    this.recursiveClassMap,
  });

  bool? aNullableBool;

  int? aNullableInt;

  int? aNullableInt64;

  double? aNullableDouble;

  Uint8List? aNullableByteArray;

  Int32List? aNullable4ByteArray;

  Int64List? aNullable8ByteArray;

  Float64List? aNullableFloatArray;

  EventEnum? aNullableEnum;

  AnotherEventEnum? anotherNullableEnum;

  String? aNullableString;

  Object? aNullableObject;

  EventAllNullableTypes? allNullableTypes;

  List<Object?>? list;

  List<String?>? stringList;

  List<int?>? intList;

  List<double?>? doubleList;

  List<bool?>? boolList;

  List<EventEnum?>? enumList;

  List<Object?>? objectList;

  List<List<Object?>?>? listList;

  List<Map<Object?, Object?>?>? mapList;

  List<EventAllNullableTypes?>? recursiveClassList;

  Map<Object?, Object?>? map;

  Map<String?, String?>? stringMap;

  Map<int?, int?>? intMap;

  Map<EventEnum?, EventEnum?>? enumMap;

  Map<Object?, Object?>? objectMap;

  Map<int?, List<Object?>?>? listMap;

  Map<int?, Map<Object?, Object?>?>? mapMap;

  Map<int?, EventAllNullableTypes?>? recursiveClassMap;

  List<Object?> _toList() {
    return <Object?>[
      aNullableBool,
      aNullableInt,
      aNullableInt64,
      aNullableDouble,
      aNullableByteArray,
      aNullable4ByteArray,
      aNullable8ByteArray,
      aNullableFloatArray,
      aNullableEnum,
      anotherNullableEnum,
      aNullableString,
      aNullableObject,
      allNullableTypes,
      list,
      stringList,
      intList,
      doubleList,
      boolList,
      enumList,
      objectList,
      listList,
      mapList,
      recursiveClassList,
      map,
      stringMap,
      intMap,
      enumMap,
      objectMap,
      listMap,
      mapMap,
      recursiveClassMap,
    ];
  }

  Object encode() {
    return _toList();
  }

  static EventAllNullableTypes decode(Object result) {
    result as List<Object?>;
    return EventAllNullableTypes(
      aNullableBool: result[0] as bool?,
      aNullableInt: result[1] as int?,
      aNullableInt64: result[2] as int?,
      aNullableDouble: result[3] as double?,
      aNullableByteArray: result[4] as Uint8List?,
      aNullable4ByteArray: result[5] as Int32List?,
      aNullable8ByteArray: result[6] as Int64List?,
      aNullableFloatArray: result[7] as Float64List?,
      aNullableEnum: result[8] as EventEnum?,
      anotherNullableEnum: result[9] as AnotherEventEnum?,
      aNullableString: result[10] as String?,
      aNullableObject: result[11],
      allNullableTypes: result[12] as EventAllNullableTypes?,
      list: result[13] as List<Object?>?,
      stringList: (result[14] as List<Object?>?)?.cast<String?>(),
      intList: (result[15] as List<Object?>?)?.cast<int?>(),
      doubleList: (result[16] as List<Object?>?)?.cast<double?>(),
      boolList: (result[17] as List<Object?>?)?.cast<bool?>(),
      enumList: (result[18] as List<Object?>?)?.cast<EventEnum?>(),
      objectList: (result[19] as List<Object?>?)?.cast<Object?>(),
      listList: (result[20] as List<Object?>?)?.cast<List<Object?>?>(),
      mapList: (result[21] as List<Object?>?)?.cast<Map<Object?, Object?>?>(),
      recursiveClassList:
          (result[22] as List<Object?>?)?.cast<EventAllNullableTypes?>(),
      map: result[23] as Map<Object?, Object?>?,
      stringMap:
          (result[24] as Map<Object?, Object?>?)?.cast<String?, String?>(),
      intMap: (result[25] as Map<Object?, Object?>?)?.cast<int?, int?>(),
      enumMap: (result[26] as Map<Object?, Object?>?)
          ?.cast<EventEnum?, EventEnum?>(),
      objectMap:
          (result[27] as Map<Object?, Object?>?)?.cast<Object?, Object?>(),
      listMap:
          (result[28] as Map<Object?, Object?>?)?.cast<int?, List<Object?>?>(),
      mapMap: (result[29] as Map<Object?, Object?>?)
          ?.cast<int?, Map<Object?, Object?>?>(),
      recursiveClassMap: (result[30] as Map<Object?, Object?>?)
          ?.cast<int?, EventAllNullableTypes?>(),
    );
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  bool operator ==(Object other) {
    if (other is! EventAllNullableTypes || other.runtimeType != runtimeType) {
      return false;
    }
    if (identical(this, other)) {
      return true;
    }
    return _deepEquals(encode(), other.encode());
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  int get hashCode => Object.hashAll(_toList());
}

sealed class PlatformEvent {}

class IntEvent extends PlatformEvent {
  IntEvent({
    required this.value,
  });

  int value;

  List<Object?> _toList() {
    return <Object?>[
      value,
    ];
  }

  Object encode() {
    return _toList();
  }

  static IntEvent decode(Object result) {
    result as List<Object?>;
    return IntEvent(
      value: result[0]! as int,
    );
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  bool operator ==(Object other) {
    if (other is! IntEvent || other.runtimeType != runtimeType) {
      return false;
    }
    if (identical(this, other)) {
      return true;
    }
    return _deepEquals(encode(), other.encode());
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  int get hashCode => Object.hashAll(_toList());
}

class StringEvent extends PlatformEvent {
  StringEvent({
    required this.value,
  });

  String value;

  List<Object?> _toList() {
    return <Object?>[
      value,
    ];
  }

  Object encode() {
    return _toList();
  }

  static StringEvent decode(Object result) {
    result as List<Object?>;
    return StringEvent(
      value: result[0]! as String,
    );
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  bool operator ==(Object other) {
    if (other is! StringEvent || other.runtimeType != runtimeType) {
      return false;
    }
    if (identical(this, other)) {
      return true;
    }
    return _deepEquals(encode(), other.encode());
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  int get hashCode => Object.hashAll(_toList());
}

class BoolEvent extends PlatformEvent {
  BoolEvent({
    required this.value,
  });

  bool value;

  List<Object?> _toList() {
    return <Object?>[
      value,
    ];
  }

  Object encode() {
    return _toList();
  }

  static BoolEvent decode(Object result) {
    result as List<Object?>;
    return BoolEvent(
      value: result[0]! as bool,
    );
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  bool operator ==(Object other) {
    if (other is! BoolEvent || other.runtimeType != runtimeType) {
      return false;
    }
    if (identical(this, other)) {
      return true;
    }
    return _deepEquals(encode(), other.encode());
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  int get hashCode => Object.hashAll(_toList());
}

class DoubleEvent extends PlatformEvent {
  DoubleEvent({
    required this.value,
  });

  double value;

  List<Object?> _toList() {
    return <Object?>[
      value,
    ];
  }

  Object encode() {
    return _toList();
  }

  static DoubleEvent decode(Object result) {
    result as List<Object?>;
    return DoubleEvent(
      value: result[0]! as double,
    );
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  bool operator ==(Object other) {
    if (other is! DoubleEvent || other.runtimeType != runtimeType) {
      return false;
    }
    if (identical(this, other)) {
      return true;
    }
    return _deepEquals(encode(), other.encode());
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  int get hashCode => Object.hashAll(_toList());
}

class ObjectsEvent extends PlatformEvent {
  ObjectsEvent({
    required this.value,
  });

  Object value;

  List<Object?> _toList() {
    return <Object?>[
      value,
    ];
  }

  Object encode() {
    return _toList();
  }

  static ObjectsEvent decode(Object result) {
    result as List<Object?>;
    return ObjectsEvent(
      value: result[0]!,
    );
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  bool operator ==(Object other) {
    if (other is! ObjectsEvent || other.runtimeType != runtimeType) {
      return false;
    }
    if (identical(this, other)) {
      return true;
    }
    return _deepEquals(encode(), other.encode());
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  int get hashCode => Object.hashAll(_toList());
}

class EnumEvent extends PlatformEvent {
  EnumEvent({
    required this.value,
  });

  EventEnum value;

  List<Object?> _toList() {
    return <Object?>[
      value,
    ];
  }

  Object encode() {
    return _toList();
  }

  static EnumEvent decode(Object result) {
    result as List<Object?>;
    return EnumEvent(
      value: result[0]! as EventEnum,
    );
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  bool operator ==(Object other) {
    if (other is! EnumEvent || other.runtimeType != runtimeType) {
      return false;
    }
    if (identical(this, other)) {
      return true;
    }
    return _deepEquals(encode(), other.encode());
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  int get hashCode => Object.hashAll(_toList());
}

class ClassEvent extends PlatformEvent {
  ClassEvent({
    required this.value,
  });

  EventAllNullableTypes value;

  List<Object?> _toList() {
    return <Object?>[
      value,
    ];
  }

  Object encode() {
    return _toList();
  }

  static ClassEvent decode(Object result) {
    result as List<Object?>;
    return ClassEvent(
      value: result[0]! as EventAllNullableTypes,
    );
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  bool operator ==(Object other) {
    if (other is! ClassEvent || other.runtimeType != runtimeType) {
      return false;
    }
    if (identical(this, other)) {
      return true;
    }
    return _deepEquals(encode(), other.encode());
  }

  @override
  // ignore: avoid_equals_and_hash_code_on_mutable_classes
  int get hashCode => Object.hashAll(_toList());
}

class _PigeonCodec extends StandardMessageCodec {
  const _PigeonCodec();
  @override
  void writeValue(WriteBuffer buffer, Object? value) {
    if (value is int) {
      buffer.putUint8(4);
      buffer.putInt64(value);
    } else if (value is EventEnum) {
      buffer.putUint8(129);
      writeValue(buffer, value.index);
    } else if (value is AnotherEventEnum) {
      buffer.putUint8(130);
      writeValue(buffer, value.index);
    } else if (value is EventAllNullableTypes) {
      buffer.putUint8(131);
      writeValue(buffer, value.encode());
    } else if (value is IntEvent) {
      buffer.putUint8(132);
      writeValue(buffer, value.encode());
    } else if (value is StringEvent) {
      buffer.putUint8(133);
      writeValue(buffer, value.encode());
    } else if (value is BoolEvent) {
      buffer.putUint8(134);
      writeValue(buffer, value.encode());
    } else if (value is DoubleEvent) {
      buffer.putUint8(135);
      writeValue(buffer, value.encode());
    } else if (value is ObjectsEvent) {
      buffer.putUint8(136);
      writeValue(buffer, value.encode());
    } else if (value is EnumEvent) {
      buffer.putUint8(137);
      writeValue(buffer, value.encode());
    } else if (value is ClassEvent) {
      buffer.putUint8(138);
      writeValue(buffer, value.encode());
    } else {
      super.writeValue(buffer, value);
    }
  }

  @override
  Object? readValueOfType(int type, ReadBuffer buffer) {
    switch (type) {
      case 129:
        final int? value = readValue(buffer) as int?;
        return value == null ? null : EventEnum.values[value];
      case 130:
        final int? value = readValue(buffer) as int?;
        return value == null ? null : AnotherEventEnum.values[value];
      case 131:
        return EventAllNullableTypes.decode(readValue(buffer)!);
      case 132:
        return IntEvent.decode(readValue(buffer)!);
      case 133:
        return StringEvent.decode(readValue(buffer)!);
      case 134:
        return BoolEvent.decode(readValue(buffer)!);
      case 135:
        return DoubleEvent.decode(readValue(buffer)!);
      case 136:
        return ObjectsEvent.decode(readValue(buffer)!);
      case 137:
        return EnumEvent.decode(readValue(buffer)!);
      case 138:
        return ClassEvent.decode(readValue(buffer)!);
      default:
        return super.readValueOfType(type, buffer);
    }
  }
}

const StandardMethodCodec pigeonMethodCodec =
    StandardMethodCodec(_PigeonCodec());

Stream<int> streamInts({String instanceName = ''}) {
  if (instanceName.isNotEmpty) {
    instanceName = '.$instanceName';
  }
  const EventChannel streamIntsChannel = EventChannel(
      'dev.flutter.pigeon.pigeon_integration_tests.EventChannelMethods.streamInts',
      pigeonMethodCodec);
  return streamIntsChannel.receiveBroadcastStream().map((dynamic event) {
    return event as int;
  });
}

Stream<PlatformEvent> streamEvents({String instanceName = ''}) {
  if (instanceName.isNotEmpty) {
    instanceName = '.$instanceName';
  }
  const EventChannel streamEventsChannel = EventChannel(
      'dev.flutter.pigeon.pigeon_integration_tests.EventChannelMethods.streamEvents',
      pigeonMethodCodec);
  return streamEventsChannel.receiveBroadcastStream().map((dynamic event) {
    return event as PlatformEvent;
  });
}
