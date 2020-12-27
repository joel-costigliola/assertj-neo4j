// Nodes

CREATE
  (Sample:Types {
    // Simple types
    type_boolean: true,
    type_string: 'sample-value',
    type_long: 42,
    type_double: 3.14159265358979324,
    type_date: date('2014-01-02'),
    type_datetime: datetime('2014-01-02T03:04:05.666+0100'),
    type_localdatetime: localdatetime('2014-01-02T03:04:05'),
    type_time: time('030405.666+0100'),
    type_localtime: localtime('03:04:05.666'),
    type_duration: duration('P14DT16H12M'),
    type_point_2d: point({ x: 6, y: 9 }),
    type_point_3d: point({ x: 6, y: 6, z: 6 }),
    // List composite types
    type_list_boolean: [ true, false ],
    type_list_string: [ x IN range(0, 10) | left("sample-test-types", 7 + x) ],
    type_list_long: range(0, 10),
    type_list_double: [ x IN range(0, 10) | x ^ 3 / 2 ],
    type_list_date: [ x IN range(0, 10) | date('2014-01-02') + duration({days: x}) ],
    type_list_datetime: [ x IN range(0, 10) | datetime('2014-01-02T03:04:05.666+0100') + duration({days: x, minutes: 2 * x}) ],
    type_list_localdatetime: [ x IN range(0, 10) | localdatetime('2014-01-02T03:04:05') + duration({days: x, minutes: 2 * x}) ],
    type_list_time: [ x IN range(0, 10) | time('030405.666+0100') + duration({minutes: x}) ],
    type_list_localtime: [ x IN range(0, 10) | localtime('03:04:05.666') + duration({minutes: x}) ],
    type_list_duration: [ x IN range(0, 10) | duration({days: x, minutes: 2 * x}) ],
    type_list_point_2d: [ x IN range(0, 10) | point({ x: 6 + x, y: 9 + 2 * x }) ],
    type_list_point_3d: [ x IN range(0, 10) | point({ x: 6 + x, y: 6 + 2 * x, z: 6 + 3 * x }) ]
    //    type_list_map: [
    //      { k_str: 'v1', k_long: 42 },
    //      { k_str: 'v2', k_long: 69 }
    //    ],
    //    type_map: {
    //      k_str: 'v1',
    //      k_long: 42
    //    }
})
