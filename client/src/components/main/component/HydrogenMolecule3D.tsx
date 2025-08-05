import React, { useRef, useMemo } from "react";
import { Canvas, useFrame } from "@react-three/fiber";
import * as THREE from "three";

interface HydrogenAtomProps {
  position: [number, number, number];
  color?: string;
}

function HydrogenAtom({ position, color = "#4A7CD5" }: HydrogenAtomProps) {
  const meshRef = useRef<THREE.Mesh>(null!);

  useFrame((state) => {
    if (meshRef.current) {
      // 미묘한 진동 효과
      meshRef.current.position.y += Math.sin(state.clock.elapsedTime * 2) * 0.001;
    }
  });

  return (
    <mesh ref={meshRef} position={position} castShadow receiveShadow>
      <sphereGeometry args={[0.08, 24, 24]} />
      <meshPhysicalMaterial
        color={color}
        metalness={0.1}
        roughness={0.2}
        clearcoat={1}
        clearcoatRoughness={0.1}
        envMapIntensity={1}
        transparent={true}
        opacity={0.9}
      />
    </mesh>
  );
}

interface BondProps {
  startPos: [number, number, number];
  endPos: [number, number, number];
}

function Bond({ startPos, endPos }: BondProps) {
  const meshRef = useRef<THREE.Mesh>(null!);

  const { position, rotation, length } = useMemo(() => {
    const start = new THREE.Vector3(...startPos);
    const end = new THREE.Vector3(...endPos);
    const length = start.distanceTo(end);
    const position: [number, number, number] = [
      (start.x + end.x) / 2,
      (start.y + end.y) / 2,
      (start.z + end.z) / 2,
    ];

    // 본드 방향 계산
    const direction = new THREE.Vector3().subVectors(end, start).normalize();
    const up = new THREE.Vector3(0, 1, 0);
    const quaternion = new THREE.Quaternion().setFromUnitVectors(up, direction);
    const euler = new THREE.Euler().setFromQuaternion(quaternion);

    return {
      position,
      rotation: [euler.x, euler.y, euler.z] as [number, number, number],
      length,
    };
  }, [startPos, endPos]);

  useFrame((state) => {
    if (meshRef.current) {
      // 미묘한 스케일 애니메이션
      const scale = 1 + Math.sin(state.clock.elapsedTime * 3) * 0.02;
      meshRef.current.scale.setScalar(scale);
    }
  });

  return (
    <mesh ref={meshRef} position={position} rotation={rotation} castShadow receiveShadow>
      <cylinderGeometry args={[0.015, 0.015, length, 12]} />
      <meshPhysicalMaterial
        color="#66B92F"
        metalness={0.3}
        roughness={0.3}
        clearcoat={0.8}
        clearcoatRoughness={0.2}
        transparent={true}
        opacity={0.9}
      />
    </mesh>
  );
}

interface SingleMoleculeProps {
  position: [number, number, number];
  animationType: string;
  opacity?: number;
}

function SingleMolecule({ position, animationType, opacity = 0.25 }: SingleMoleculeProps) {
  const groupRef = useRef<THREE.Group>(null!);

  useFrame((state, delta) => {
    if (groupRef.current) {
      // 애니메이션 타입에 따른 다른 움직임 패턴
      const time = state.clock.elapsedTime;
      
      switch (animationType) {
        case "elegantFloat1":
          groupRef.current.rotation.x = Math.sin(time * 0.3) * 0.2;
          groupRef.current.rotation.y = Math.cos(time * 0.4) * 0.3;
          groupRef.current.rotation.z = Math.sin(time * 0.2) * 0.1;
          groupRef.current.position.y = position[1] + Math.sin(time * 0.5) * 0.1;
          break;
        case "elegantFloat2":
          groupRef.current.rotation.x = Math.cos(time * 0.25) * 0.25;
          groupRef.current.rotation.y = Math.sin(time * 0.35) * 0.2;
          groupRef.current.rotation.z = Math.cos(time * 0.3) * 0.15;
          groupRef.current.position.y = position[1] + Math.cos(time * 0.6) * 0.08;
          break;
        case "elegantFloat3":
          groupRef.current.rotation.x = Math.sin(time * 0.4) * 0.15;
          groupRef.current.rotation.y = Math.cos(time * 0.3) * 0.25;
          groupRef.current.rotation.z = Math.sin(time * 0.35) * 0.2;
          groupRef.current.position.y = position[1] + Math.sin(time * 0.7) * 0.12;
          break;
        case "elegantFloat4":
          groupRef.current.rotation.x = Math.cos(time * 0.32) * 0.18;
          groupRef.current.rotation.y = Math.sin(time * 0.28) * 0.22;
          groupRef.current.rotation.z = Math.cos(time * 0.25) * 0.16;
          groupRef.current.position.y = position[1] + Math.cos(time * 0.45) * 0.09;
          break;
        case "elegantFloat5":
          groupRef.current.rotation.x = Math.sin(time * 0.38) * 0.22;
          groupRef.current.rotation.y = Math.cos(time * 0.42) * 0.18;
          groupRef.current.rotation.z = Math.sin(time * 0.28) * 0.25;
          groupRef.current.position.y = position[1] + Math.sin(time * 0.55) * 0.11;
          break;
        case "elegantFloat6":
          groupRef.current.rotation.x = Math.cos(time * 0.35) * 0.2;
          groupRef.current.rotation.y = Math.sin(time * 0.4) * 0.16;
          groupRef.current.rotation.z = Math.cos(time * 0.32) * 0.19;
          groupRef.current.position.y = position[1] + Math.cos(time * 0.5) * 0.1;
          break;
      }
    }
  });

  const bondDistance = 0.225; // 45px를 3D 스케일로 변환

  return (
    <group ref={groupRef} position={position}>
      <group opacity={opacity}>
        {/* 첫 번째 수소 원자 */}
        <HydrogenAtom position={[-bondDistance / 2, 0, 0]} />
        
        {/* 두 번째 수소 원자 */}
        <HydrogenAtom position={[bondDistance / 2, 0, 0]} />
        
        {/* 본드 (결합) */}
        <Bond 
          startPos={[-bondDistance / 2, 0, 0]} 
          endPos={[bondDistance / 2, 0, 0]} 
        />
      </group>
    </group>
  );
}

interface HydrogenMolecule3DProps {
  width?: string;
  height?: string;
}

export default function HydrogenMolecule3D({
  width = "100%",
  height = "100%",
}: HydrogenMolecule3DProps) {
  // 현재 2D 분자 위치를 3D 좌표로 변환
  const molecules = [
    {
      // top: "15%", right: "12%" -> 3D 좌표
      position: [1.5, 1.2, Math.random() * 0.4 - 0.2] as [number, number, number],
      animationType: "elegantFloat1",
    },
    {
      // top: "25%", right: "8%" -> 3D 좌표  
      position: [1.7, 0.8, Math.random() * 0.4 - 0.2] as [number, number, number],
      animationType: "elegantFloat2",
    },
    {
      // top: "35%", right: "15%" -> 3D 좌표
      position: [1.3, 0.4, Math.random() * 0.4 - 0.2] as [number, number, number],
      animationType: "elegantFloat3",
    },
    {
      // top: "45%", left: "8%" -> 3D 좌표
      position: [-1.7, 0, Math.random() * 0.4 - 0.2] as [number, number, number],
      animationType: "elegantFloat4",
    },
    {
      // top: "55%", left: "12%" -> 3D 좌표
      position: [-1.5, -0.4, Math.random() * 0.4 - 0.2] as [number, number, number],
      animationType: "elegantFloat5",
    },
    {
      // top: "50%", left: "5%" -> 3D 좌표
      position: [-1.8, -0.2, Math.random() * 0.4 - 0.2] as [number, number, number],
      animationType: "elegantFloat6",
    },
  ];

  return (
    <div style={{ width, height, position: "absolute", top: 0, left: 0 }}>
      <Canvas
        camera={{ position: [0, 0, 3], fov: 45 }}
        style={{ background: "transparent" }}
        gl={{ alpha: true, antialias: true }}
      >
        {/* 조명 설정 - 부드럽고 자연스럽게 */}
        <ambientLight intensity={0.6} />
        <directionalLight
          position={[5, 5, 5]}
          intensity={0.8}
          castShadow
          shadow-mapSize-width={1024}
          shadow-mapSize-height={1024}
          shadow-camera-far={50}
          shadow-camera-left={-10}
          shadow-camera-right={10}
          shadow-camera-top={10}
          shadow-camera-bottom={-10}
        />
        <pointLight position={[-3, -3, -3]} intensity={0.3} />

        {/* 수소 분자들 */}
        {molecules.map((molecule, index) => (
          <SingleMolecule
            key={index}
            position={molecule.position}
            animationType={molecule.animationType}
            opacity={0.25}
          />
        ))}
      </Canvas>
    </div>
  );
}