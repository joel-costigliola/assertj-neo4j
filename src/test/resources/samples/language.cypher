// WARNING :
// ALL OF VALUES DESCRIBE BELLOW IS NOT REAL AND HAVE BEEN DEFINE ONLY TO HAVE
// A DATASET TO RUN SOME INTEGRATION TESTS

// Nodes

CREATE
  (Java:Language {name: 'Java'}),
  (Go:Language {name: 'Golang'}),
  (Kotlin:Language {name: 'Kotlin'}),
  (Python:Language {name: 'Python'}),
  (Shell:Language {name: 'Shell'}),
  (SQL:Language {name: 'SQL'}),
  (Scala:Language {name: 'Scala'})

// Generated using https://randomuser.me/
CREATE
  (HLecomte:Person:Developer {
    name:          'Hanspeter Lecomte',
    date_of_birth: date('1975-10-07'),
    picture:       'https://randomuser.me/api/portraits/men/37.jpg'
  }),
  (JAnderson:Person:Developer {
    name:          'Judy Anderson',
    date_of_birth: date('1984-12-26'),
    picture:       'https://randomuser.me/api/portraits/women/68.jpg'
  }),
  (MKing:Person:Developer {
    name:          'Mike King',
    date_of_birth: date('1994-07-24'),
    picture:       'https://randomuser.me/api/portraits/men/41.jpg'
  }),
  (ZCote:Person:Developer {
    name:          'Zachary Côté',
    date_of_birth: date('1956-03-14'),
    picture:       'https://randomuser.me/api/portraits/men/35.jpg'
  }),
  (RGros:Person:Sysadmin {
    name:    'Rumeysa Gros',
    picture: 'https://randomuser.me/api/portraits/women/61.jpg'
  }),
  (FJordan:Person:SRE {
    name:          'Florence Jordan',
    date_of_birth: date('1992-03-06')
  }),
  (KTurner:Person:DBA {
    name: 'Keira Turner'
  })

CREATE
  (Assertj_AssertjCore:Repo {
    name: 'assertj-core',
    owner: 'assertj',
    url: 'https://github.com/assertj/assertj-core',
    creation_date: localdatetime('2013-03-14T16:18:49'),
    onboarding_duration: duration({  minutes: 3 }),
    active_branches: ['main', 'dev' ],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  }),
  (Neo4j_Neo4j:Repo {
    name: 'neo4j',
    owner: 'neo4j',
    url: 'https://github.com/neo4j/neo4j',
    creation_date: localdatetime('2012-11-12T08:46:15'),
    onboarding_duration: duration({  minutes: 15 }),
    active_branches: ['master', 'dev' ],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  }),
  (JunitTeam_Junit5:Repo {
    name: 'junit5',
    owner: 'junit-team',
    url: 'https://github.com/junit-team/junit5',
    creation_date: localdatetime('2015-01-11T19:06:10'),
    onboarding_duration: duration({  minutes: 10 }),
    active_branches: ['main', 'dev', 'junit-4-support'],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  }),
  (Urfave_Cli:Repo {
    name: 'cli',
    owner: 'urfave',
    url: 'https://github.com/urfave/cli',
    creation_date: localdatetime('2013-07-13T19:32:06'),
    onboarding_duration: duration({  minutes: 5 }),
    active_branches: ['master', 'dev', '2.0' ],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  }),
  (Labstack_Echo:Repo {
    name: 'echo',
    owner: 'labstack',
    url: 'https://github.com/labstack/echo',
    creation_date: localdatetime('2015-03-01T17:43:01'),
    onboarding_duration: duration({  minutes: 10 }),
    active_branches: ['master' ],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  }),
  (Ktorio_Ktor:Repo {
    name: 'ktor',
    owner: 'ktorio',
    url: 'https://github.com/ktorio/ktor',
    creation_date: localdatetime('2015-08-03T16:49:36'),
    onboarding_duration: duration({  minutes: 20 }),
    active_branches: ['main', 'jvm', 'native' ],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  }),
  (Kubernetes_Kubernetes:Repo {
    name: 'kubernetes',
    owner: 'kubernetes',
    url: 'https://github.com/kubernetes/kubernetes',
    creation_date: localdatetime('2014-06-06T22:56:04'),
    onboarding_duration: duration({  minutes: 30 }),
    active_branches: ['main', 'dev', '1.19.X', '1.20.X' ],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  }),
  (Grafana_Tempo:Repo {
    name: 'tempo',
    owner: 'grafana',
    url: 'https://github.com/grafana/tempo',
    creation_date: localdatetime('2020-01-24T18:05:02'),
    onboarding_duration: duration({  minutes: 25 }),
    active_branches: ['main'],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  }),
  (Prometheus_Prometheus:Repo {
    name: 'prometheus',
    owner: 'prometheus',
    url: 'https://github.com/prometheus/prometheus',
    creation_date: localdatetime('2012-11-24T11:14:12'),
    onboarding_duration: duration({  minutes: 25 }),
    active_branches: ['master'],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  }),
  (Pallets_Flask:Repo {
    name: 'flask',
    owner: 'pallets',
    url: 'https://github.com/pallets/flask',
    creation_date: localdatetime('2010-04-06T11:11:59'),
    onboarding_duration: duration({  minutes: 20 }),
    active_branches: ['main', 'python-2.7', 'python-3'],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  }),
  (Pallets_Click:Repo {
    name: 'click',
    owner: 'pallets',
    url: 'https://github.com/pallets/click',
    creation_date: localdatetime('2014-04-24T09:52:19'),
    onboarding_duration: duration({  minutes: 5 }),
    active_branches: ['master', 'python-2.7', 'python-3'],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  }),
  (Sstephenson_Bats:Repo {
    name: 'bats',
    owner: 'sstephenson',
    url: 'https://github.com/sstephenson/bats',
    creation_date: localdatetime('2011-12-28T18:40:33'),
    onboarding_duration: duration({  minutes: 3 }),
    active_branches: ['master'],
    stats_items: ['branches', 'tags', 'stars', 'fork'],
    stats_values: [17, 42, 3450, 345]
  })

// Some relationships
CREATE
  (HLecomte)-[:KNOWS {level: 5}]->(Go),
  (HLecomte)-[:KNOWS {level: 4}]->(Java),
  (JAnderson)-[:KNOWS {level: 5}]->(Java),
  (JAnderson)-[:KNOWS {level: 4}]->(Kotlin),
  (MKing)-[:KNOWS {level: 3}]->(Java),
  (MKing)-[:KNOWS {level: 2}]->(Go),
  (MKing)-[:KNOWS {level: 4}]->(Kotlin),
  (MKing)-[:KNOWS {level: 2}]->(Python),
  (ZCote)-[:KNOWS {level: 5}]->(Python),
  (ZCote)-[:KNOWS {level: 2}]->(Java),
  (RGros)-[:KNOWS {level: 5}]->(Go),
  (RGros)-[:KNOWS {level: 4}]->(Shell),
  (RGros)-[:KNOWS {level: 3}]->(Python),
  (FJordan)-[:KNOWS {level: 5}]->(Shell),
  (FJordan)-[:KNOWS {level: 4}]->(Python),
  (KTurner)-[:KNOWS {level: 5}]->(SQL),
  (KTurner)-[:KNOWS {level: 2}]->(Python),
  (KTurner)-[:KNOWS {level: 1}]->(Java)

CREATE
  (Assertj_AssertjCore)-[:IS {percent: ['99.7']}]->(Java),
  (Assertj_AssertjCore)-[:IS {percent: ['0.3']}]->(Shell),
  (Neo4j_Neo4j)-[:IS {percent: ['76.8']}]->(Java),
  (Neo4j_Neo4j)-[:IS {percent: ['0.2']}]->(Shell),
  (JunitTeam_Junit5)-[:IS {percent: ['97.5']}]->(Java),
  (JunitTeam_Junit5)-[:IS {percent: ['2.1']}]->(Kotlin),
  (Urfave_Cli)-[:IS {percent: ['99.8']}]->(Go),
  (Urfave_Cli)-[:IS {percent: ['0.2']}]->(Shell),
  (Ktorio_Ktor)-[:IS {percent: ['0.2']}]->(Shell),
  (Kubernetes_Kubernetes)-[:IS {percent: ['90.3']}]->(Go),
  (Kubernetes_Kubernetes)-[:IS {percent: ['2.8']}]->(Shell),
  (Pallets_Click)-[:IS {percent: ['100']}]->(Python),
  (Pallets_Flask)-[:IS {percent: ['99.9']}]->(Python)
