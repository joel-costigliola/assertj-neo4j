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
    date_of_birth: '1975-10-07T06:48:59.527Z',
    picture:       'https://randomuser.me/api/portraits/men/37.jpg'
  }),
  (JAnderson:Person:Developer {
    name:          'Judy Anderson',
    date_of_birth: '1984-12-26T15:30:30.434Z',
    picture:       'https://randomuser.me/api/portraits/women/68.jpg'
  }),
  (MKing:Person:Developer {
    name:          'Mike King',
    date_of_birth: '1994-07-24T22:53:30.778Z',
    picture:       'https://randomuser.me/api/portraits/men/41.jpg'
  }),
  (ZCote:Person:Developer {
    name:          'Zachary Côté',
    date_of_birth: '1956-03-14T14:08:38.472Z',
    picture:       'https://randomuser.me/api/portraits/men/35.jpg'
  }),
  (RGros:Person:Sysadmin {
    name:    'Rumeysa Gros',
    picture: 'https://randomuser.me/api/portraits/women/61.jpg'
  }),
  (FJordan:Person:SRE {
    name:          'Florence Jordan',
    date_of_birth: '1992-03-06T08:28:52.108Z'
  }),
  (KTurner:Person:DBA {
    name: 'Keira Turner'
  })

CREATE
  (Assertj_AssertjCore:Repo {
    url: 'https://github.com/assertj/assertj-core'
  }),
  (Neo4j_Neo4j:Repo {
    url: 'https://github.com/neo4j/neo4j'
  }),
  (JunitTeam_Junit5:Repo {
    url: 'https://github.com/junit-team/junit5'
  }),
  (Urfave_Cli:Repo {
    url: 'https://github.com/urfave/cli'
  }),
  (Labstack_Echo:Repo {
    url: 'https://github.com/labstack/echo'
  }),
  (Ktorio_Ktor:Repo {
    url: 'https://github.com/ktorio/ktor'
  }),
  (Kubernetes_Kubernetes:Repo {
    url: 'https://github.com/kubernetes/kubernetes'
  }),
  (Grafana_Tempo:Repo {
    url: 'https://github.com/grafana/tempo'
  }),
  (Prometheus_Prometheus:Repo {
    url: 'https://github.com/prometheus/prometheus'
  }),
  (Pallets_Flask:Repo {
    url: 'https://github.com/pallets/flask/'
  }),
  (Pallets_Click:Repo {
    url: 'https://github.com/pallets/click/'
  }),
  (Sstephenson_Bats:Repo {
    url: 'https://github.com/sstephenson/bats'
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
